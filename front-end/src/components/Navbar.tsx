import React from "react";
import { Container, Nav, Navbar } from "react-bootstrap";
import { Link } from "react-router-dom";

const NavbarComponent = () => {
  return (
    <Navbar expand="lg" className="navbar-dark bg-dark">
      <Container>
        <Navbar.Brand as={Link} to="/home" className="text-light">
          Tuna03-dev
        </Navbar.Brand>
        <Navbar.Toggle aria-controls="basic-navbar-nav" />
        <Navbar.Collapse className="" id="basic-navbar-nav">
          <Nav className="me-auto">
            <Nav.Link as={Link} to="/home" className="text-light">
              Home
            </Nav.Link>
            <Nav.Link as={Link} to="/management/users" className="text-light">
              Manage User
            </Nav.Link>
          </Nav>
          <Nav.Link as={Link} to="/login" className=" text-light">
            Login
          </Nav.Link>
        </Navbar.Collapse>
      </Container>
    </Navbar>
  );
};

export default NavbarComponent;
