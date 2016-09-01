package uk.com.a1ms.network.dto;

/**
 * Created by priju.jacobpaul on 7/07/16.
 */
public class GroupDetails {

    private String name;
    private String idUser;
    private String id;
    private data data;

    private class data {

        private String code;
        private String dateCreated;
        private String id;
        private String ids;
        private boolean isActiSent;
        private boolean isActive;
        private boolean isAdmin;
        private boolean isDeleted;
        private boolean isRetailer;
        private String name;
        private String password;
        private String username;


        public String getCode() {
            return code;
        }

        public void setCode(String code) {
            this.code = code;
        }

        public String getDateCreated() {
            return dateCreated;
        }

        public void setDateCreated(String dateCreated) {
            this.dateCreated = dateCreated;
        }

        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
        }

        public String getIds() {
            return ids;
        }

        public void setIds(String ids) {
            this.ids = ids;
        }

        public boolean isActiSent() {
            return isActiSent;
        }

        public void setActiSent(boolean actiSent) {
            isActiSent = actiSent;
        }

        public boolean isActive() {
            return isActive;
        }

        public void setActive(boolean active) {
            isActive = active;
        }

        public boolean isAdmin() {
            return isAdmin;
        }

        public void setAdmin(boolean admin) {
            isAdmin = admin;
        }

        public boolean isDeleted() {
            return isDeleted;
        }

        public void setDeleted(boolean deleted) {
            isDeleted = deleted;
        }

        public boolean isRetailer() {
            return isRetailer;
        }

        public void setRetailer(boolean retailer) {
            isRetailer = retailer;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public String getPassword() {
            return password;
        }

        public void setPassword(String password) {
            this.password = password;
        }

        public String getUsername() {
            return username;
        }

        public void setUsername(String username) {
            this.username = username;
        }
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getIdUser() {
        return idUser;
    }

    public void setIdUser(String idUser) {
        this.idUser = idUser;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public data getData() {
        return data;
    }

    public void setData(data data) {
        this.data = data;
    }
}
