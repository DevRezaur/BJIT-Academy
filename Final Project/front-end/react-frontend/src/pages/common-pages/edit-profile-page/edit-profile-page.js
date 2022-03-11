import React, { useCallback, useContext, useEffect, useRef, useState } from "react";
import "./edit-profile-page.scss";
import { LoginContext } from "../../../contexts/login-context";
import axios from "axios";
import Constant from "../../../shared/config/constant";
import { useForm } from "react-hook-form";
import { yupResolver } from "@hookform/resolvers/yup";
import * as yup from "yup";
import { useNavigate } from "react-router-dom";
import { toast } from "react-toastify";

const EditProfilePage = () => {
  const { authInfoContext } = useContext(LoginContext);
  const [authInfo, setAuthInfo] = authInfoContext;
  const [userInfo, setUserInfo] = useState(null);
  const hiddenFileInput = useRef(null);
  let navigate = useNavigate();

  const schema = yup.object().shape({
    userId: yup.string().required(),
    username: yup.string().email().required(),
    password: yup.string().required().min(3).max(12),
    role: yup.string().required(),
    fullName: yup.string().required(),
    gender: yup.string().required(),
    contact: yup.string().required().matches(/^\d+$/, "contact should only contain numbers"),
    presentAddress: yup.string(),
    permanentAddress: yup.string(),
    department: yup.string(),
  });

  const {
    register,
    handleSubmit,
    formState: { errors },
  } = useForm({
    resolver: yupResolver(schema),
  });

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

  const updateUserInfo = async (data) => {
    data = { ...data, imageUrl: userInfo.imageUrl };

    await axios({
      method: "POST",
      url: "http://localhost:8080/api/v1/user/profile/update",
      headers: {
        Authorization: `Bearer ${authInfo.token}`,
      },
      data: data,
    })
      .then((response) => {
        if (response.data) {
          setUserInfo(response.data);
          toast.success("Profile Updated Successfully!");
        }
      })
      .catch((error) => {
        if (!error.response) toast.error("Failed to Update Profile!");
        else if (error.response.status === 403) logout();
      });
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
    <div className="edit-profile-page">
      <div className="container">
        <div className="grid-wrapper">
          <div className="left">
            <img src={userInfo?.imageUrl} alt="" />
            <button className="button-primary" onClick={handleClick}>
              Update Avatar
            </button>
            <input type="file" ref={hiddenFileInput} onChange={(e) => uploadAvatar(e)} style={{ display: "none" }} />
          </div>
          {userInfo && (
            <div className="right">
              <form onSubmit={handleSubmit(updateUserInfo)}>
                <section className="profile-section">
                  <h3>Personal Info</h3>
                  <div className="info-wrapper">
                    <div className="info">
                      <label>Employee ID</label>
                      <input className="no-edit" type="text" defaultValue={userInfo?.userId} {...register("userId")} spellCheck="false" readOnly />
                    </div>
                    <div className="info">
                      <label>Fullname</label>
                      <input className="no-edit" type="text" defaultValue={userInfo.fullName} {...register("fullName")} spellCheck="false" readOnly />
                    </div>
                    <div className="info">
                      <label>Department</label>
                      <input
                        className="no-edit"
                        type="text"
                        defaultValue={userInfo?.department}
                        {...register("department")}
                        spellCheck="false"
                        readOnly
                      />
                    </div>
                    <div className="info">
                      <label>Gender</label>
                      <div className="radio-button">
                        <input type="radio" name="gender" defaultValue="Male" defaultChecked={userInfo?.gender === "Male"} {...register("gender")} />
                        <p>Male</p>

                        <input
                          type="radio"
                          name="gender"
                          defaultValue="Female"
                          defaultChecked={userInfo?.gender === "Female"}
                          {...register("gender")}
                        />
                        <p>Female</p>
                      </div>
                    </div>
                    <div className="info">
                      <label>Contact</label>
                      <div>
                        <input type="text" defaultValue={userInfo?.contact} {...register("contact")} />
                        <p className="warning">{errors.contact?.message}</p>
                      </div>
                    </div>
                    <div className="info">
                      <label>Present Address</label>
                      <input type="text" defaultValue={userInfo?.presentAddress} {...register("presentAddress")} spellCheck="false" />
                    </div>
                    <div className="info">
                      <label>Permanent Address</label>
                      <input type="text" defaultValue={userInfo?.permanentAddress} {...register("permanentAddress")} spellCheck="false" />
                    </div>
                  </div>
                  <h3>Account Info</h3>
                  <div className="info-wrapper">
                    <div className="info">
                      <label>Email</label>
                      <input
                        className="no-edit"
                        type="text"
                        defaultValue={userInfo?.username}
                        {...register("username")}
                        spellCheck="false"
                        readOnly
                      />
                    </div>

                    <div className="info">
                      <label>Password</label>
                      <div>
                        <input type="text" defaultValue={userInfo?.password} {...register("password")} />
                        <p className="warning">{errors.password?.message}</p>
                      </div>
                    </div>
                    <div className="info">
                      <label>Role</label>
                      <input className="no-edit" type="text" defaultValue={userInfo?.role} {...register("role")} readOnly />
                    </div>
                  </div>
                </section>

                <button className="button-primary save-button">Save Changes</button>
              </form>
            </div>
          )}
        </div>
      </div>
    </div>
  );
};

export default EditProfilePage;
