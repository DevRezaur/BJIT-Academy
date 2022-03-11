import React, { useContext } from "react";
import "./user-list.scss";
import { LoginContext } from "../../contexts/login-context";
import { useNavigate } from "react-router-dom";

const UserList = (props) => {
  let userList = props.userList;
  const { authInfoContext } = useContext(LoginContext);
  const [authInfo] = authInfoContext;
  const navigate = useNavigate();

  const manageUser = (userId) => {
    if (authInfo && authInfo.roles[0] === "ROLE_ADMIN") navigate("/admin-manage-user/" + userId);
  };

  return (
    <>
      {userList && (
        <table>
          <tbody>
            <tr>
              <th>Id</th>
              <th>Fullname</th>
              <th>Email</th>
              <th>Department</th>
              <th>Role</th>
            </tr>

            {userList.map((user) => (
              <tr key={user.userId} onClick={() => manageUser(user.userId)}>
                <td>{user.userId}</td>
                <td>{user.fullName}</td>
                <td>{user.username}</td>
                <td>{user.department}</td>
                <td>{user.role}</td>
              </tr>
            ))}
          </tbody>
        </table>
      )}
    </>
  );
};

export default UserList;
