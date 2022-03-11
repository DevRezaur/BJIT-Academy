import React, { useContext } from "react";
import BannerImage from "../../../shared/image/SVG/admin_dashboard.svg";
import AddUser from "../../../shared/image/SVG/add_users.svg";
import UpdateUser from "../../../shared/image/SVG/update_user.svg";
import FindUser from "../../../shared/image/SVG/find_users.svg";
import AddBatch from "../../../shared/image/SVG/add_batch.svg";
import AllBatch from "../../../shared/image/SVG/all_batch.svg";
import "./admin-dashboard.scss";
import { LoginContext } from "../../../contexts/login-context";
import { useNavigate } from "react-router-dom";

const AdminDashboard = () => {
  const { authInfoContext } = useContext(LoginContext);
  const authInfo = authInfoContext[0];
  const navigator = useNavigate();

  const redirectTo = (path) => {
    navigator(path);
  };

  return (
    <div className="admin-dashboard">
      <section className="top-section">
        <div className="container">
          <div className="grid-wrapper">
            <div className="left-div">
              <p className="top-line">Hello {authInfo.fullName}</p>
              <h3>Welcome to Admin Dashboard</h3>
              <p className="desc">
                This is the admin dashboard of BJIT Academy. From here you can add, update and manage the system users and their information. You can
                also add, update and view all the running courses and manage their activities. Please be aware of any unchanges that you make as it
                affects the entire system.
              </p>
            </div>
            <div className="right-div">
              <img src={BannerImage} alt="" />
            </div>
          </div>
        </div>
      </section>

      <section className="mid-section">
        <div className="container">
          <h3>Manage System Users</h3>
          <hr />

          <div className="grid-wrapper">
            <div className="card" onClick={() => redirectTo("/admin-add-user")}>
              <img src={AddUser} alt="" />
              <p>Add New User</p>
            </div>
            <div className="card" onClick={() => redirectTo("/admin-manage-user")}>
              <img src={UpdateUser} alt="" />
              <p>Manage User Data</p>
            </div>
            <div className="card" onClick={() => redirectTo("/list-user")}>
              <img src={FindUser} alt="" />
              <p>List All User</p>
            </div>
          </div>
        </div>
      </section>

      <section className="mid-section">
        <div className="container">
          <h3>Manage Traing Batches</h3>
          <hr />

          <div className="grid-wrapper">
            <div className="card" onClick={() => redirectTo("/admin-add-batch")}>
              <img src={AddBatch} alt="" />
              <p>Add New Batch</p>
            </div>
            <div className="card" onClick={() => redirectTo("/admin-list-batch")}>
              <img src={AllBatch} alt="" />
              <p>All Batches</p>
            </div>
          </div>
        </div>
      </section>

      <div className="bottom-section">&nbsp;</div>
    </div>
  );
};

export default AdminDashboard;
