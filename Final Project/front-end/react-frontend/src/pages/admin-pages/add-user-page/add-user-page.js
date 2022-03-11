import React, { useContext } from "react";
import { LoginContext } from "../../../contexts/login-context";
import "./add-user-page.scss";
import { useForm } from "react-hook-form";
import { yupResolver } from "@hookform/resolvers/yup";
import * as yup from "yup";
import { useNavigate } from "react-router-dom";
import axios from "axios";
import { toast } from "react-toastify";
import Constant from "../../../shared/config/constant";

const AddUserPage = () => {
  const { authInfoContext } = useContext(LoginContext);
  const [authInfo, setAuthInfo] = authInfoContext;
  let navigate = useNavigate();

  const schema = yup.object().shape({
    userId: yup.string().required(),
    username: yup.string().email().required(),
    password: yup.string().required().min(3).max(12),
    role: yup.string().required(),
    fullName: yup.string().required(),
    gender: yup.string().required().typeError("gender is a required field"),
    contact: yup.string().required().matches(/^\d+$/, "contact should only contain numbers"),
    presentAddress: yup.string(),
    permanentAddress: yup.string(),
    department: yup.string().required(),
  });

  const {
    register,
    handleSubmit,
    formState: { errors },
  } = useForm({
    resolver: yupResolver(schema),
  });

  const logout = () => {
    alert("Session Expired");
    localStorage.clear();
    setAuthInfo(null);
    navigate("/");
  };

  const addUserInfo = async (data) => {
    data = { ...data, imageUrl: "https://devrezaur.com/File-Bucket/image/default_user.png" };

    await axios({
      method: "POST",
      url: Constant.BACKEND_BASE_URL + "/user/add",
      headers: {
        Authorization: `Bearer ${authInfo.token}`,
      },
      data: data,
    })
      .then((response) => {
        if (response.data) {
          toast.success("User Added Successfully!");
        }
      })
      .catch((error) => {
        if (!error.response) toast.error("Failed to Add User!");
        else if (error.response.status === 403) logout();
      });
  };

  return (
    <div className="add-user-page">
      <div className="container">
        <h3>Add New Employee</h3>
        <hr />

        <form onSubmit={handleSubmit(addUserInfo)}>
          <div className="info-wrapper">
            <div className="left-div">
              <div className="info">
                <label>Employee ID</label>
                <input type="text" {...register("userId")} spellCheck="false" />
                <p className="warning">{errors.userId?.message}</p>
              </div>

              <div className="info">
                <label>BJIT Email</label>
                <input type="text" {...register("username")} spellCheck="false" />
                <p className="warning">{errors.username?.message}</p>
              </div>

              <div className="info">
                <label>Default Password</label>
                <input type="password" {...register("password")} spellCheck="false" />
                <p className="warning">{errors.password?.message}</p>
              </div>

              <div className="info">
                <label>System Level Role</label>
                <select {...register("role")}>
                  <option value="">Select Role Type</option>
                  <option value="ROLE_USER">User</option>
                  <option value="ROLE_ADMIN">Admin</option>
                </select>
                <p className="warning">{errors.role?.message}</p>
              </div>

              <div className="info">
                <label>Full Name</label>
                <input type="text" {...register("fullName")} spellCheck="false" />
                <p className="warning">{errors.fullName?.message}</p>
              </div>
            </div>

            <div className="right-div">
              <div className="info">
                <label>Gender</label>
                <div className="radio-button">
                  <input type="radio" name="gender" defaultValue="Male" {...register("gender")} />
                  <p>Male</p>
                  <input type="radio" name="gender" defaultValue="Female" {...register("gender")} />
                  <p>Female</p>
                </div>
                <p className="warning">{errors.gender?.message}</p>
              </div>

              <div className="info">
                <label>Contact</label>
                <input type="text" {...register("contact")} spellCheck="false" />
                <p className="warning">{errors.contact?.message}</p>
              </div>

              <div className="info">
                <label>Present Address</label>
                <input type="text" {...register("presentAddress")} spellCheck="false" />
                <p className="warning">{errors.presentAddress?.message}</p>
              </div>

              <div className="info">
                <label>Permanent Address</label>
                <input type="text" {...register("permanentAddress")} spellCheck="false" />
                <p className="warning">{errors.permanentAddress?.message}</p>
              </div>

              <div className="info">
                <label>Department</label>
                <select {...register("department")}>
                  <option value="">Select Department</option>
                  <option value="Java">Java</option>
                  <option value="Blockchain">Blockchain</option>
                  <option value="SQA">SQA</option>
                  <option value="Management">Management</option>
                </select>
                <p className="warning">{errors.department?.message}</p>
              </div>
            </div>
          </div>
          <button type="submit" className="button-primary">
            Add User
          </button>
        </form>
      </div>
    </div>
  );
};

export default AddUserPage;
