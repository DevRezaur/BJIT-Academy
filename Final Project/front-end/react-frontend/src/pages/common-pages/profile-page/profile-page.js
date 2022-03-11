import React, { useCallback, useContext, useRef } from "react";
import "./profile-page.scss";
import { useNavigate } from "react-router-dom";
import { LoginContext } from "../../../contexts/login-context";
import { useEffect, useState } from "react/cjs/react.development";
import axios from "axios";
import Constant from "../../../shared/config/constant";
import { toast } from "react-toastify";

const ProfilePage = () => {
  const { authInfoContext } = useContext(LoginContext);
  const [authInfo, setAuthInfo] = authInfoContext;
  const [userInfo, setUserInfo] = useState(null);
  const hiddenFileInput = useRef(null);
  let navigate = useNavigate();

  const logout = useCallback(() => {
    alert("Session Expired");
    localStorage.clear();
    setAuthInfo(null);
    navigate("/");
  }, [setAuthInfo, navigate]);

  useEffect(() => {
    axios({
      method: "GET",
      url: Constant.BACKEND_BASE_URL + "/user/profile",
      headers: {
        Authorization: `Bearer ${authInfo.token}`,
      },
    })
      .then((response) => {
        setUserInfo(response.data);
      })
      .catch((error) => {
        if (!error.response) toast.error(error);
        else if (error.response.status === 403) logout();
      });
  }, [authInfo, logout]);

  const goToEditProfilePage = () => {
    navigate("edit");
  };

  const handleClick = (event) => {
    hiddenFileInput.current.click();
  };

  const uploadAvatar = async (event) => {
    const image = event.target.files[0];
    const formData = new FormData();
    formData.append("sendimage", image);

    await axios
      .post("https://devrezaur.com/File-Bucket/image-upload.php", formData, {
        headers: {
          "Content-Type": "multipart/form-data",
        },
      })
      .then((response) => {
        if (response.data) {
          let imageUrl = `https://devrezaur.com/File-Bucket/image/${event.target.files[0].name}`;
          setUserInfo({ ...userInfo, imageUrl: imageUrl });
          updateUserImageUrl(imageUrl);
        }
      })
      .catch((error) => {
        toast.error(error);
      });
  };

  const updateUserImageUrl = async (imageUrl) => {
    await axios({
      method: "POST",
      url: "http://localhost:8080/api/v1/user/profile/imageUrl/update",
      headers: {
        Authorization: `Bearer ${authInfo.token}`,
      },
      data: {
        userId: userInfo.userId,
        username: userInfo.username,
        imageUrl: imageUrl,
      },
    })
      .then((response) => {
        if (response.data) {
          toast.success("Image Uploaded Successfully!");
        }
      })
      .catch((error) => {
        if (!error.response) toast.error("Failed to Upload Image!");
        else if (error.response.status === 403) logout();
      });
  };

  return (
    <div className="profile-page">
      {userInfo && (
        <div className="grid-wrapper container">
          <div className="left">
            <img src={userInfo?.imageUrl} alt="" />
            <button className="button-primary" onClick={handleClick}>
              Update Avatar
            </button>
            <input type="file" ref={hiddenFileInput} onChange={(e) => uploadAvatar(e)} style={{ display: "none" }} />
          </div>
          <div className="right">
            <section className="profile-section">
              <h3>Personal Info</h3>
              <div className="info-wrapper">
                <div className="info">
                  <p className="label">Employee ID</p>
                  <p className="data">{userInfo?.userId}</p>
                </div>
                <div className="info">
                  <p className="label">Fullname</p>
                  <p className="data">{userInfo?.fullName}</p>
                </div>
                <div className="info">
                  <p className="label">Department</p>
                  <p className="data">{userInfo?.department}</p>
                </div>
                <div className="info">
                  <p className="label">Gender</p>
                  <p className="data">{userInfo?.gender}</p>
                </div>
                <div className="info">
                  <p className="label">Contact</p>
                  <p className="data">{userInfo?.contact}</p>
                </div>
                <div className="info">
                  <p className="label">Present Address</p>
                  <p className="data">{userInfo?.presentAddress}</p>
                </div>
                <div className="info">
                  <p className="label">Permanent Address</p>
                  <p className="data">{userInfo?.permanentAddress}</p>
                </div>
              </div>
              <h3>Account Info</h3>
              <div className="info-wrapper">
                <div className="info">
                  <p className="label">Email</p>
                  <p className="data">{userInfo?.username}</p>
                </div>
                <div className="info">
                  <p className="label">Password</p>
                  <p className="data">(Not Visible)</p>
                </div>
                <div className="info">
                  <p className="label">Role</p>
                  <p className="data">{userInfo?.role}</p>
                </div>
              </div>
            </section>
            <button className="button-primary" onClick={goToEditProfilePage}>
              <i className="far fa-edit"></i>Edit Profile
            </button>
          </div>
        </div>
      )}
    </div>
  );
};
export default ProfilePage;
