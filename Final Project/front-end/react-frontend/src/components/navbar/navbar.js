import "./navbar.scss";
import { getAdminNav, getUserNav, ToggleSidebar } from "../../services/nav-service";
import { useEffect, useState, useContext, useCallback } from "react";
import { Link, useNavigate } from "react-router-dom";
import { LoginContext } from "../../contexts/login-context";

const Navbar = () => {
  const [menu, setMenu] = useState([]);
  const { authInfoContext } = useContext(LoginContext);
  const [authInfo, setAuthInfo] = authInfoContext;
  let navigate = useNavigate();

  const logout = useCallback(() => {
    localStorage.clear();
    setAuthInfo(null);
    navigate("/");
  }, [setAuthInfo, navigate]);

  useEffect(() => {
    if (authInfo)
      if (authInfo.roles[0] === "ROLE_ADMIN") setMenu(getAdminNav());
      else if (authInfo?.roles[0] === "ROLE_USER") setMenu(getUserNav);
  }, [authInfo, logout]);

  const toggleSidebar = () => {
    ToggleSidebar();
  };

  const login = () => {
    navigate("/login");
  };

  return (
    <>
      {/*Navbar Starts*/}
      <nav>
        <div className="branding">
          <i className="fas fa-bars" onClick={toggleSidebar}></i>
          <p>BJIT Academy</p>
        </div>
        {authInfo ? (
          <div className="menu">
            <p>{authInfo.fullName}</p>
            <button className="button-outline" onClick={logout}>
              Logout
            </button>
          </div>
        ) : (
          <div className="menu">
            <button className="button-outline" onClick={login}>
              Login
            </button>
          </div>
        )}
      </nav>
      {/* Navbar Ends */}

      {/* Backdrop */}
      <div className="hidden backdrop" onClick={toggleSidebar}></div>

      {/* Sidebar Starts */}
      <section id="side-bar" className="hidden">
        <div className="branding">
          <p>BJIT Academy</p>
          <i className="fas fa-times" onClick={toggleSidebar}></i>
        </div>
        {authInfo ? (
          <div className="sidebar-wrapper">
            <div className="user-info">
              <p>{authInfo.fullName}</p>
            </div>
            <div className="menu-items" onClick={toggleSidebar}>
              {menu.map((item) => (
                <div key={item.name}>
                  <Link to={item.link}>{item.name}</Link>
                </div>
              ))}
              <button className="button-outline" onClick={logout}>
                Logout
              </button>
            </div>
          </div>
        ) : (
          <div className="sidebar-wrapper">
            <div className="user-info">
              <p>No User</p>
              <p>(Please Login)</p>
            </div>
            <div className="menu-items" onClick={toggleSidebar}>
              <button className="button-outline" onClick={login}>
                Login
              </button>
            </div>
          </div>
        )}
      </section>
      {/* Sidebar Ends */}
    </>
  );
};

export default Navbar;
