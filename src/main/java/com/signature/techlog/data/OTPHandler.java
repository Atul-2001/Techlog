package com.signature.techlog.data;

import com.signature.techlog.database.DatabaseFactory;
import com.signature.techlog.model.OTP;
import com.signature.techlog.model.User;

import jakarta.persistence.PersistenceException;
import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.hibernate.Session;
import org.hibernate.Transaction;

public class OTPHandler {

    private static final Logger LOGGER = LogManager.getLogger(OTPHandler.class);

    public OTPHandler() { }

    public boolean saveOrUpdateOTP(OTP otp) {
        if (otp != null) {
            boolean response;
            try (Session session = DatabaseFactory.getSession()) {
                Transaction tx = null;
                try {
                    tx = session.beginTransaction();
                    session.saveOrUpdate(otp);
                    tx.commit();
                    response = Boolean.TRUE;
                } catch (Exception ex) {
                    LOGGER.log(Level.ERROR, ex.getMessage(), ex);
                    if (tx != null) {
                        if (tx.isActive()) {
                            tx.rollback();
                        }
                    }
                    response = Boolean.FALSE;
                }
            } catch (IllegalStateException | PersistenceException ex) {
                LOGGER.log(Level.ERROR, "Process : Persist OTP in Database | Cause : Exception occured while handling transaction failure. | Loss : Failed to rollback Transaction", ex);
                response = Boolean.FALSE;
            }
            return response;
        } else {
            return Boolean.FALSE;
        }
    }

    public OTP getOTP(String sessionID, User user) {
        if (sessionID != null && user != null) {
            OTP response;
            try (Session session = DatabaseFactory.getSession()) {
                response = session.get(OTP.class, sessionID);
                if (!response.getUserID().equals(user.getId())) {
                    LOGGER.log(Level.INFO, "User ID didn't match!");
                    response = null;
                }
            } catch (Exception ex) {
                LOGGER.log(Level.ERROR, ex.getMessage(), ex);
                response = null;
            }
            return response;
        } else {
            return null;
        }
    }

}