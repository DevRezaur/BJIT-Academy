export const ToggleSidebar = () => {
  const sidebar = document.querySelector("#side-bar");
  const backdrop = document.querySelector(".backdrop");
  const html = document.querySelector("html");

  sidebar?.classList.toggle("hidden");
  backdrop?.classList.toggle("hidden");

  if (!sidebar?.classList.contains("hidden") && html) {
    html.style.overflowY = "hidden";
  } else if (html) {
    html.style.overflowY = "auto";
  }
};

export const getAdminNav = () => {
  return [
    { name: "Dashboard", link: "/admin-dashboard" },
    { name: "Profile", link: "/admin-profile" },
    { name: "Add New User", link: "/admin-add-user" },
    { name: "Manage User Info", link: "/admin-manage-user" },
    { name: "View All User", link: "/list-user" },
    { name: "Add New Batch", link: "/admin-add-batch" },
    { name: "View All Batches", link: "/admin-list-batch" },
  ];
};

export const getUserNav = () => {
  return [
    { name: "Dashboard", link: "/user-dashboard" },
    { name: "Profile", link: "/user-profile" },
    { name: "All Users", link: "/list-user" },
  ];
};
