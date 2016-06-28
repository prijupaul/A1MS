package uk.com.a1ms.network.dto;

/**
 * Created by priju.jacobpaul on 24/06/16.
 */
public class UserDetails {



    private String token;
    private user user;
    private String Message;

    public class user {

        private String username;
        private String name;
        private String dateCreated;
        private boolean isActive;
        private boolean isRetailer;
        private String code;
        private boolean isDeleted;
        private String id;

        public String getUsername() {
            return username;
        }

        public void setUsername(String username) {
            this.username = username;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public String getDateCreated() {
            return dateCreated;
        }

        public void setDateCreated(String dateCreated) {
            this.dateCreated = dateCreated;
        }

        public boolean isActive() {
            return isActive;
        }

        public void setActive(boolean active) {
            isActive = active;
        }

        public boolean isRetailer() {
            return isRetailer;
        }

        public void setRetailer(boolean retailer) {
            isRetailer = retailer;
        }

        public String getCode() {
            return code;
        }

        public void setCode(String code) {
            this.code = code;
        }

        public boolean isDeleted() {
            return isDeleted;
        }

        public void setDeleted(boolean deleted) {
            isDeleted = deleted;
        }

        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
        }
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public UserDetails.user getUser() {
        return user;
    }

    public void setUser(UserDetails.user user) {
        this.user = user;
    }

    public String getMessage() {
        return Message;
    }

    public void setMessage(String message) {
        Message = message;
    }
}
