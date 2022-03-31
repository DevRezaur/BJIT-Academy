import axios from "axios";
import React, { useCallback, useContext, useEffect, useState } from "react";
import { useNavigate, useParams } from "react-router-dom";
import DashboardComponent from "../../../components/dashboard-component/dashboard-component";
import DiscussionBoard from "../../../components/discussion-board/discussion-board";
import EnrollNewUser from "../../../components/enroll-new-user/enroll-new-user";
import EnrolledUserList from "../../../components/enrolled-user-list/enrolled-user-list";
import { LoginContext } from "../../../contexts/login-context";
import Constant from "../../../shared/config/constant";
import "./batch-dashboard.scss";

const BatchDashboard = () => {
  const { authInfoContext } = useContext(LoginContext);
  const [authInfo, setAuthInfo] = authInfoContext;
  const [batch, setBatch] = useState({});
  let navigate = useNavigate();
  const { batchId } = useParams();
  const [isDashboardSelected, setIsDashboardSelected] = useState(true);
  const [isEnrolledUsersSelected, setIsEnrolledUsersSelected] = useState(false);
  const [isAddNewUserSelected, setIsAddNewUserSelected] = useState(false);
  const [isDisscussionSelected, setIsDisscussionSelected] = useState(false);

  const handleActionBar = (action) => {
    if (action === "dashboard") {
      setIsDashboardSelected(true);
      setIsDisscussionSelected(false);
      setIsEnrolledUsersSelected(false);
      setIsAddNewUserSelected(false);
    } else if (action === "discussion") {
      setIsDashboardSelected(false);
      setIsDisscussionSelected(true);
      setIsEnrolledUsersSelected(false);
      setIsAddNewUserSelected(false);
    } else if (action === "enrolled-users") {
      setIsDashboardSelected(false);
      setIsDisscussionSelected(false);
      setIsEnrolledUsersSelected(true);
      setIsAddNewUserSelected(false);
    } else if (action === "add-user") {
      setIsDashboardSelected(false);
      setIsDisscussionSelected(false);
      setIsEnrolledUsersSelected(false);
      setIsAddNewUserSelected(true);
    }
  };

  const logout = useCallback(() => {
    alert("Session Expired");
    localStorage.clear();
    setAuthInfo(null);
    navigate("/");
  }, [setAuthInfo, navigate]);

  useEffect(() => {
    axios({
      method: "GET",
      url: Constant.BACKEND_BASE_URL + "/batch/" + batchId,
      headers: {
        Authorization: `Bearer ${authInfo.token}`,
      },
    })
      .then((response) => {
        setBatch(response.data);
      })
      .catch((error) => {
        if (error.response.status === 403) logout();
      });
  }, [authInfo, batchId, logout]);

  return (
    <>
      <div className="batch-dashboard container">
        <div className="banner">
          <div className="image-section">
            <img src={batch.imageUrl} alt="" />
          </div>

          <div className="info-section">
            <p className="title">{batch.batchName}</p>
            <p className="description">{batch.description}</p>
          </div>
        </div>

        <div className="control-panel">
          <div className="button-wrapper">
            <button className={isDashboardSelected ? "button-primary" : "button-outline"} type="button" onClick={() => handleActionBar("dashboard")}>
              Dashboard
            </button>
            <button
              className={isDisscussionSelected ? "button-primary" : "button-outline"}
              type="button"
              onClick={() => handleActionBar("discussion")}
            >
              Discussion
            </button>
            <button
              className={isEnrolledUsersSelected ? "button-primary" : "button-outline"}
              type="button"
              onClick={() => handleActionBar("enrolled-users")}
            >
              Enrolled Users
            </button>
            {authInfo.roles[0] === "ROLE_ADMIN" && (
              <button className={isAddNewUserSelected ? "button-primary" : "button-outline"} onClick={() => handleActionBar("add-user")}>
                Add New User
              </button>
            )}
          </div>

          <hr />
        </div>

        {isDashboardSelected && <DashboardComponent />}

        {isEnrolledUsersSelected && <EnrolledUserList />}

        {isAddNewUserSelected && <EnrollNewUser />}

        {isDisscussionSelected && <DiscussionBoard />}
      </div>
    </>
  );
};

export default BatchDashboard;
