import axios from "axios";
import React, { useCallback, useContext, useEffect, useState } from "react";
import { useNavigate, useParams } from "react-router-dom";
import { LoginContext } from "../../contexts/login-context";
import Constant from "../../shared/config/constant";
import { toast } from "react-toastify";
import "./enrolled-user-list.scss";

const EnrolledUserList = () => {
  const { authInfoContext } = useContext(LoginContext);
  const [authInfo, setAuthInfo] = authInfoContext;
  const [userList, setUserList] = useState(null);
  const { batchId } = useParams();
  let navigate = useNavigate();

  const logout = useCallback(() => {
    alert("Session Expired");
    localStorage.clear();
    setAuthInfo(null);
    navigate("/");
  }, [setAuthInfo, navigate]);

  const getEnrolledUsers = useCallback(() => {
    axios({
      method: "GET",
      url: Constant.BACKEND_BASE_URL + "/batch/" + batchId + "/enrolledUsers",
      headers: {
        Authorization: `Bearer ${authInfo.token}`,
      },
    })
      .then((response) => {
        setUserList(response.data);
      })
      .catch((error) => {
        if (!error.response) toast.error(error);
        else if (error.response.status === 403) logout();
      });
  }, [authInfo, batchId, logout]);

  useEffect(() => {
    getEnrolledUsers();
  }, [getEnrolledUsers]);

  const unEnrollUser = (userId) => {
    axios({
      method: "POST",
      url: Constant.BACKEND_BASE_URL + "/batch/un-enroll",
      headers: {
        Authorization: `Bearer ${authInfo.token}`,
      },
      data: {
        batchId: batchId,
        userId: userId,
      },
    })
      .then((response) => {
        getEnrolledUsers();
        toast.success("Successfully Un Enrolled");
      })
      .catch((error) => {
        if (!error.response) toast.error(error);
        else if (error.response.status === 403) logout();
      });
  };

  return (
    <>
      {userList && (
        <table className="enrolled-users">
          <tbody>
            <tr>
              <th>Id</th>
              <th>Fullname</th>
              <th>Email</th>
              <th>Department</th>
              {authInfo.roles[0] === "ROLE_ADMIN" && <th>Action</th>}
            </tr>

            {userList.map((user) => (
              <tr key={user.userId}>
                <td>{user.userId}</td>
                <td>{user.fullName}</td>
                <td>{user.username}</td>
                <td>{user.department}</td>
                {authInfo.roles[0] === "ROLE_ADMIN" && (
                  <td>
                    <button type="button" onClick={() => unEnrollUser(user.userId)}>
                      Un Enroll
                    </button>
                  </td>
                )}
              </tr>
            ))}
          </tbody>
        </table>
      )}
    </>
  );
};

export default EnrolledUserList;
