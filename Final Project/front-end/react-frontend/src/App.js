import { BrowserRouter, Routes, Route } from "react-router-dom";
import Footer from "./components/footer/footer";
import Navbar from "./components/navbar/navbar";
import AdminDashboard from "./pages/admin-pages/admin-dashboard/admin-dashboard";
import HomePage from "./pages/common-pages/home-page/home-page";
import LoginPage from "./pages/common-pages/login-page/login-page";
import UserDashboard from "./pages/user-pages/user-dashboard/user-dashboard";
import PrivateRoute from "./services/private-route";
import { LoginContext } from "./contexts/login-context";
import { useState } from "react";
import ProfilePage from "./pages/common-pages/profile-page/profile-page";
import NotFound from "./pages/common-pages/not-found/not-found";
import { useEffect } from "react/cjs/react.development";
import Constant from "./shared/config/constant";
import axios from "axios";
import { toast } from "react-toastify";
import "react-toastify/dist/ReactToastify.css";
import EditProfilePage from "./pages/common-pages/edit-profile-page/edit-profile-page";
import AddUserPage from "./pages/admin-pages/add-user-page/add-user-page";
import ManageUserInfo from "./pages/admin-pages/manage-user-info/manage-user-info";
import UserListPage from "./pages/common-pages/user-list-page/user-list-page";
import AddBatch from "./pages/admin-pages/add-batch/add-batch";
import ListBatch from "./pages/admin-pages/list-batch/list-batch";
import EditBatch from "./pages/admin-pages/edit-batch/edit-batch";
import BatchDashboard from "./pages/common-pages/batch-dashboard/batch-dashboard";

toast.configure();

function App() {
  const [authInfo, setAuthInfo] = useState(JSON.parse(localStorage.getItem("auth-info")));

  useEffect(() => {
    let interval = setInterval(() => {
      if (authInfo) {
        axios({
          method: "POST",
          url: Constant.BACKEND_BASE_URL + "/auth/refreshToken",
          data: {
            refreshToken: authInfo.refreshToken,
          },
        })
          .then((response) => {
            authInfo.token = response.data.accessToken;
            localStorage.setItem("auth-info", JSON.stringify(authInfo));
          })
          .catch((error) => {
            alert(error);
            setAuthInfo(null);
          });
      }
    }, 200000);

    return () => clearInterval(interval);
  }, [authInfo]);

  return (
    <>
      <BrowserRouter>
        <LoginContext.Provider value={{ authInfoContext: [authInfo, setAuthInfo] }}>
          <Navbar />

          <Routes>
            <Route exact path="/" element={<HomePage />} />
            <Route path="/login" element={<LoginPage />} />

            {/* User Routes */}
            <Route path="/user-dashboard" element={<PrivateRoute role={["ROLE_USER"]} component={UserDashboard} />} />
            <Route path="/user-profile" element={<PrivateRoute role={["ROLE_USER"]} component={ProfilePage} />} />
            <Route path="/user-profile/edit" element={<PrivateRoute role={["ROLE_USER"]} component={EditProfilePage} />} />

            {/* User Routes */}
            <Route path="/admin-dashboard" element={<PrivateRoute role={["ROLE_ADMIN"]} component={AdminDashboard} />} />
            <Route path="/admin-profile" element={<PrivateRoute role={["ROLE_ADMIN"]} component={ProfilePage} />} />
            <Route path="/admin-profile/edit" element={<PrivateRoute role={["ROLE_ADMIN"]} component={EditProfilePage} />} />
            <Route path="/admin-add-user" element={<PrivateRoute role={["ROLE_ADMIN"]} component={AddUserPage} />} />
            <Route path="/admin-manage-user" element={<PrivateRoute role={["ROLE_ADMIN"]} component={ManageUserInfo} />} />
            <Route path="/admin-manage-user/:userId" element={<PrivateRoute role={["ROLE_ADMIN"]} component={ManageUserInfo} />} />
            <Route path="/admin-add-batch" element={<PrivateRoute role={["ROLE_ADMIN"]} component={AddBatch} />} />
            <Route path="/admin-edit-batch/:batchId" element={<PrivateRoute role={["ROLE_ADMIN"]} component={EditBatch} />} />
            <Route path="/admin-list-batch" element={<PrivateRoute role={["ROLE_ADMIN"]} component={ListBatch} />} />

            {/* Common Routes */}
            <Route path="/batch-dashboard/:batchId" element={<PrivateRoute role={["ROLE_ADMIN", "ROLE_USER"]} component={BatchDashboard} />} />
            <Route path="/list-user" element={<PrivateRoute role={["ROLE_ADMIN", "ROLE_USER"]} component={UserListPage} />} />

            <Route path="*" element={<NotFound />} />
          </Routes>

          <Footer />
        </LoginContext.Provider>
      </BrowserRouter>
    </>
  );
}

export default App;
