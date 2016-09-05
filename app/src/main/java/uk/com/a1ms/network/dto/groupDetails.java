package uk.com.a1ms.network.dto;

import java.util.ArrayList;

import uk.com.a1ms.db.dto.A1MSUser;

/**
 * Created by priju.jacobpaul on 7/07/16.
 */
public class GroupDetails {

    private String errorMessage;
    private String responseCode;
    private data data;

    public class data {
        private String id;

        private String isActive;

        private String name;

        private String idUser;

        private A1MSUser user;

        private ArrayList<String> members;

        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
        }

        public String getIsActive() {
            return isActive;
        }

        public void setIsActive(String isActive) {
            this.isActive = isActive;
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

        public A1MSUser getUser() {
            return user;
        }

        public void setUser(A1MSUser user) {
            this.user = user;
        }

        public ArrayList<String> getMembers() {
            return members;
        }

        public void setMembers(ArrayList<String> members) {
            this.members = members;
        }

        @Override
        public String toString() {
            return "ClassPojo [id = " + id + ", isActive = " + isActive + ", name = " + name + ", idUser = " + idUser + ", user = " + user + ", members = " + members + "]";
        }

    }

    public data getData() {
        return data;
    }

    public void setData(data data) {
        this.data = data;
    }

    public String getErrorMessage() {
        return errorMessage;
    }

    public void setErrorMessage(String errorMessage) {
        this.errorMessage = errorMessage;
    }

    public String getResponseCode() {
        return responseCode;
    }

    public void setResponseCode(String responseCode) {
        this.responseCode = responseCode;
    }
}

