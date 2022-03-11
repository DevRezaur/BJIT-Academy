import "./home-page.scss";
import { Link, useNavigate } from "react-router-dom";
import Logo from "../../../shared/image/SVG/banner.svg";
import { useContext } from "react";
import { LoginContext } from "../../../contexts/login-context";

const HomePage = () => {
  const { authInfoContext } = useContext(LoginContext);
  const [authInfo] = authInfoContext;
  let navigate = useNavigate();

  const redirectToDashboard = () => {
    if (authInfo.roles[0] === "ROLE_ADMIN") navigate("/admin-dashboard");
    else if (authInfo.roles[0] === "ROLE_USER") navigate("/user-dashboard");
  };

  return (
    <>
      <section className="home-page">
        <div className="container">
          <div className="grid-wrapper">
            <div className="left-div">
              <img src={Logo} alt="" />
            </div>
            <div className="right-div">
              <h3>The Place Where Future Engineers Are Trained</h3>
              <h2>
                Welcome to <span>BJIT Academy</span>
              </h2>
              <p>
                Welcome to BJIT Academy's online portal. BJIT Academy is a sub part of BJIT Group. BJIT Academy provides all sorts of trainings to the
                freshly recruited enginners. From this online portal, BJIT Acadamy manages all of their activities via online. This portal was
                developed to eliminate the usage of Skype groups for managing training batches online.
              </p>
              {!authInfo ? (
                <button type="button" className="button-primary">
                  <Link to={"./login"}>Login to BJIT Academy</Link>
                </button>
              ) : (
                <button type="button" className="button-primary" onClick={redirectToDashboard}>
                  Go To Dashboard
                </button>
              )}
            </div>
          </div>
        </div>
      </section>
    </>
  );
};

export default HomePage;
