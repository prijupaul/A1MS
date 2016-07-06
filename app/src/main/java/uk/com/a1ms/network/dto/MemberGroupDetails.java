package uk.com.a1ms.network.dto;

import java.util.ArrayList;

/**
 * Created by priju.jacobpaul on 7/07/16.
 */
public class MemberGroupDetails {

    private ArrayList<group> groups = new ArrayList<>();

    private class group {
        private String id;
        private String idUser;
        private String name;

        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
        }

        public String getIdUser() {
            return idUser;
        }

        public void setIdUser(String idUser) {
            this.idUser = idUser;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }
    }

    public ArrayList<group> getGroups() {
        return groups;
    }

    public void setGroups(ArrayList<group> groups) {
        this.groups = groups;
    }
}
