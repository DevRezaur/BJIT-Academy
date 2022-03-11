import React, { useCallback, useContext, useEffect, useState } from "react";
import { useNavigate, useParams } from "react-router-dom";
import { LoginContext } from "../../../contexts/login-context";
import "./manage-user-info.scss";
import { useForm } from "react-hook-form";
import { yupResolver } from "@hookform/resolvers/yup";
import * as yup from "yup";
import axios from "axios";
import Constant from "../../../shared/config/constant";
import { toast } from "react-toastify";

const ManageUserInfo = () => {
  const { authInfoContext } = useContext(LoginContext);
  const [authInfo, setAuthInfo] = authInfoContext;
  const [userInfo, setUserInfo] = useState(null);
  const { userId } = useParams();
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
    department: yup.string().required(),
  });

  const {
    register,
    handleSubmit,
    formState: { errors },
    reset,
  } = useForm({
    resolver: yupResolver(schema),
  });

  const logout = useCallback(() => {
    alert("Session Expired");
    localStorage.clear();
    setAuthInfo(null);
    navigate("/");
  }, [setAuthInfo, navigate]);

  const doGetUserInfo = useCallback(
    (userId) => {
      axios({
        method: "GET",
        url: Constant.BACKEND_BASE_URL + "/user/profile/" + userId,
        headers: {
          Authorization: `Bearer ${authInfo.token}`,
        },
      })
        .then((response) => {
          setUserInfo(response.data);
          if (!response.data) toast.error("No User Data Found");
        })
        .catch((error) => {
          if (!error.response) toast.error(error);
          else if (error.response.status === 403) logout();
        });
    },
    [authInfo, logout]
  );

  useEffect(() => {
    if (userId) doGetUserInfo(userId);
  }, [userId, doGetUserInfo]);

  const getUserInfo = (e) => {
    e.preventDefault();
    let userId = e.target.elements[0].value;
    doGetUserInfo(userId);
  };

  const updateUserInfo = (data) => {
    data = { ...data, imageUrl: userInfo.imageUrl };

    axios({
      method: "POST",
      url: Constant.BACKEND_BASE_URL + "/user/update",
      headers: {
        Authorization: `Bearer ${authInfo.token}`,
      },
      data: data,
    })
      .then((response) => {
        setUserInfo(null);
        toast.success("User Information Updated");
        reset();
      })
      .catch((error) => {
        if (!error.response) toast.error(error);
        else if (error.response.status === 403) logout();
      });
  };

  return (
    <div className="manage-user-info">
      <div className="container">
        <h3>Manage User Information</h3>

        <form className="top-form" onSubmit={(e) => getUserInfo(e)}>
          <input type="number" defaultValue={userId} spellCheck="false" />
          <button type="submit" className="button-primary">
            Find User
          </button>
        </form>

        <hr />

        <form className="bottom-form" onSubmit={handleSubmit(updateUserInfo)}>
          <div className="info-wrapper">
            <div className="left-div">
              <div className="info">
                <label>Employee ID</label>
                <input type="text" defaultValue={userInfo && userInfo.userId} {...(userInfo && { ...register("userId") })} spellCheck="false" />
                <p className="warning">{errors.userId?.message}</p>
              </div>

              <div className="info">
                <label>BJIT Email</label>
                <input type="text" defaultValue={userInfo && userInfo.username} {...(userInfo && { ...register("username") })} spellCheck="false" />
                <p className="warning">{errors.username?.message}</p>
              </div>

              <div className="info">
                <label>Default Password</label>
                <input
                  type="password"
                  defaultValue={userInfo && userInfo.password}
                  {...(userInfo && { ...register("password") })}
                  spellCheck="false"
                />
                <p className="warning">{errors.password?.message}</p>
              </div>

              <div className="info">
                <label>System Level Role</label>
                <select {...(userInfo && { ...register("role") })}>
                  <option value="">Select Role</option>
                  <option value="ROLE_USER" selected={userInfo && userInfo.role === "ROLE_USER"}>
                    User
                  </option>
                  <option value="ROLE_ADMIN" selected={userInfo && userInfo.role === "ROLE_ADMIN"}>
                    Admin
                  </option>
                </select>
                <p className="warning">{errors.role?.message}</p>
              </div>

              <div className="info">
                <label>Full Name</label>
                <input type="text" defaultValue={userInfo && userInfo.fullName} {...(userInfo && { ...register("fullName") })} spellCheck="false" />
                <p className="warning">{errors.fullName?.message}</p>
              </div>
            </div>

            <div className="right-div">
              <div className="info">
                <label>Gender</label>
                <div className="radio-button">
                  {userInfo && (
                    <input type="radio" name="gender" defaultValue="Male" defaultChecked={userInfo.gender === "Male"} {...register("gender")} />
                  )}
                  <p>Male</p>

                  {userInfo && (
                    <input type="radio" name="gender" defaultValue="Female" defaultChecked={userInfo.gender === "Female"} {...register("gender")} />
                  )}
                  <p>Female</p>
                </div>
                <p className="warning">{errors.gender?.message}</p>
              </div>

              <div className="info">
                <label>Contact</label>
                <input type="text" defaultValue={userInfo && userInfo.contact} {...(userInfo && { ...register("contact") })} spellCheck="false" />
                <p className="warning">{errors.contact?.message}</p>
              </div>

              <div className="info">
                <label>Present Address</label>
                <input
                  type="text"
                  defaultValue={userInfo && userInfo.presentAddress}
                  {...(userInfo && { ...register("presentAddress") })}
                  spellCheck="false"
                />
                <p className="warning">{errors.presentAddress?.message}</p>
              </div>

              <div className="info">
                <label>Permanent Address</label>
                <input
                  type="text"
                  defaultValue={userInfo && userInfo.permanentAddress}
                  {...(userInfo && { ...register("permanentAddress") })}
                  spellCheck="false"
                />
                <p className="warning">{errors.permanentAddress?.message}</p>
              </div>

              <div className="info">
                <label>Department</label>
                <select {...(userInfo && { ...register("department") })}>
                  <option value="">Select Department</option>
                  <option value="Java" selected={userInfo && userInfo.department === "Java"}>
                    Java
                  </option>
                  <option value="Blockchain" selected={userInfo && userInfo.department === "Blockchain"}>
                    Blockchain
                  </option>
                  <option value="SQA" selected={userInfo && userInfo.department === "SQA"}>
                    SQA
                  </option>
                  <option value="Management" selected={userInfo && userInfo.department === "Management"}>
                    Management
                  </option>
                </select>
                <p className="warning">{errors.department?.message}</p>
              </div>
            </div>
          </div>
          <button type="submit" className="button-primary">
            Submit
          </button>
        </form>
      </div>
    </div>
  );
};

export default ManageUserInfo;
