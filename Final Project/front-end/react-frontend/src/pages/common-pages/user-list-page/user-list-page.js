import axios from "axios";
import React, { useCallback, useContext, useEffect, useState } from "react";
import { useNavigate } from "react-router-dom";
import { toast } from "react-toastify";
import UserList from "../../../components/user-list/user-list";
import { LoginContext } from "../../../contexts/login-context";
import Constant from "../../../shared/config/constant";
import "./user-list-page.scss";

const UserListPage = () => {
  const { authInfoContext } = useContext(LoginContext);
  const [authInfo, setAuthInfo] = authInfoContext;
  const [userList, setUserList] = useState(null);
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
      url: Constant.BACKEND_BASE_URL + "/user/all",
      headers: {
        Authorization: `Bearer ${authInfo.token}`,
      },
    })
      .then((response) => {
        setUserList(response.data);
        if (!response.data) toast.error("No User Data Found");
      })
      .catch((error) => {
        if (!error.response) toast.error(error);
        else if (error.response.status === 403) logout();
      });
  }, [authInfo, logout]);

  return (
    <div className="user-list-page">
      <div className="container">
        <div className="user-list-section">
          <h3>All Users of the System</h3>
          <hr />
          {userList && <UserList userList={userList} />}
        </div>
      </div>
    </div>
  );
};

export default UserListPage;
