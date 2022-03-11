import "./footer.scss";

const Footer = () => {
  return (
    <>
      <footer>
        <div className="wrapper">
          <div className="branding">
            <i className="fas fa-university"></i>
            <p>BJIT Academy</p>
          </div>
          <p>Developed By &nbsp;&copy; DevRezaur</p>
          <div className="icons">
            <a href="https://www.facebook.com/BJITLTD" targer="_blank" aria-label="Facebook">
              <i className="fab fa-facebook"></i>
            </a>
            <a href="https://twitter.com/bjitltd" targer="_blank" aria-label="Twitter">
              <i className="fab fa-twitter"></i>
            </a>
            <a href="https://www.linkedin.com/company/bjit" targer="_blank" aria-label="LinkedIn">
              <i className="fab fa-linkedin"></i>
            </a>
          </div>
        </div>
      </footer>
    </>
  );
};

export default Footer;
