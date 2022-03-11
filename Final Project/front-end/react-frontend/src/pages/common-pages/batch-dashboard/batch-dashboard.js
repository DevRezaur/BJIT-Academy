import React, { useContext } from "react";
import { useState } from "react/cjs/react.development";
import DashboardComponent from "../../../components/dashboard-component/dashboard-component";
import DiscussionBoard from "../../../components/discussion-board/discussion-board";
import EnrollNewUser from "../../../components/enroll-new-user/enroll-new-user";
import EnrolledUserList from "../../../components/enrolled-user-list/enrolled-user-list";
import { LoginContext } from "../../../contexts/login-context";
import "./batch-dashboard.scss";

const BatchDashboard = () => {
  const { authInfoContext } = useContext(LoginContext);
  const authInfo = authInfoContext[0];
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

  return (
    <>
      <div className="batch-dashboard container">
        <div className="banner">
          <div className="image-section">
            <img src="https://devrezaur.com/File-Bucket/image/spring.jpg" alt="" />
          </div>

          <div className="info-section">
            <p className="title">Java Batch 01</p>
            <p className="description">
              This is a sample description of Java Batch 01. This batch started at 1 October 2021, and is expected to end at 31 December, 2021.
            </p>
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
