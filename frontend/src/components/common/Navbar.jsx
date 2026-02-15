import React from 'react';
import { Link, useNavigate } from 'react-router-dom';
import { useAuth } from '../../context/AuthContext';
import './Navbar.css';

const Navbar = () => {
    const { user, logout, isAuthenticated } = useAuth();
    const navigate = useNavigate();

    const handleLogout = () => {
        logout();
        navigate('/login');
    };

    return (
        <nav className="navbar">
            <div className="navbar-container">
                <Link to="/" className="navbar-brand">
                    URL Shortener SaaS
                </Link>

                <div className="navbar-menu">
                    {isAuthenticated() ? (
                        <>
                            <Link to="/dashboard" className="nav-link">
                                Dashboard
                            </Link>
                            <span className="nav-user">
                                {user?.name}
                            </span>
                            <button onClick={handleLogout} className="btn btn-outline">
                                Logout
                            </button>
                        </>
                    ) : (
                        <>
                            <Link to="/login" className="btn btn-outline">
                                Login
                            </Link>
                            <Link to="/signup" className="btn btn-primary">
                                Sign Up
                            </Link>
                        </>
                    )}
                </div>
            </div>
        </nav>
    );
};

export default Navbar;
