package packet;

import client.users.UserRole;

import java.io.Serializable;

/**
 * Response from server to user trying to join a session.
 */
public class UserJoinRsp extends Message implements Serializable {

    public final Boolean isApproved;
    public final UserRole userRole;

    public UserJoinRsp(Boolean isApproved, UserRole userRole) {
        this.isApproved = isApproved;
        this.userRole = userRole;
    }
}
