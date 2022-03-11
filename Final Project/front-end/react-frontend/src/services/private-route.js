import React, { useContext } from "react";
import { Navigate } from "react-router-dom";
import { LoginContext } from "../contexts/login-context";

const PrivateRoute = ({ component: RouteComponent, role }) => {
  const { authInfoContext } = useContext(LoginContext);
  const [authInfo] = authInfoContext;

  let access = false;

  if (authInfo && role.includes(authInfo.roles[0])) access = true;

  return access ? <RouteComponent /> : <Navigate to="/" />;
};

export default PrivateRoute;
