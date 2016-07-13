package uk.com.a1ms.db.mock;

import android.database.sqlite.SQLiteDatabase;

import uk.com.a1ms.db.A1MSUsersFieldsDataSource;
import uk.com.a1ms.db.dto.A1MSUser;

/**
 * Created by priju.jacobpaul on 28/06/16.
 */
public class AddMockUsers {

    public static void addMockA1MSUsers(A1MSUsersFieldsDataSource dataSource, SQLiteDatabase sqLiteDatabase){

        A1MSUser a1MSUser = new A1MSUser();
        a1MSUser.setEmail("info@a1ms-uk.com");
        a1MSUser.setMobile("01234 567 8901");
        a1MSUser.setName("Echo Mate");
        a1MSUser.setEditable(false);
        dataSource.insertA1MSUser(sqLiteDatabase,a1MSUser);

        A1MSUser a1MSUser1 = new A1MSUser();
        a1MSUser1.setEmail("priju@a1ms-uk.com");
        a1MSUser1.setMobile("01224 567 8901");
        a1MSUser1.setName("Priju");
        a1MSUser1.setEditable(true);
        dataSource.insertA1MSUser(sqLiteDatabase,a1MSUser1);


        A1MSUser a1MSUser2 = new A1MSUser();
        a1MSUser2.setEmail("sanjay@a1ms-uk.com");
        a1MSUser2.setMobile("01224 567 8902");
        a1MSUser2.setName("Sanjay");
        a1MSUser2.setEditable(true);
        dataSource.insertA1MSUser(sqLiteDatabase,a1MSUser2);

        A1MSUser a1MSUser3 = new A1MSUser();
        a1MSUser3.setEmail("simon@a1ms-uk.com");
        a1MSUser3.setMobile("01224 567 8903");
        a1MSUser3.setName("Simon");
        a1MSUser.setEditable(true);
        dataSource.insertA1MSUser(sqLiteDatabase,a1MSUser3);

        A1MSUser a1MSUser4 = new A1MSUser();
        a1MSUser4.setEmail("priju@a1ms-uk.com");
        a1MSUser4.setMobile("01224 567 8904");
        a1MSUser4.setName("Kajanth");
        a1MSUser4.setEditable(true);
        dataSource.insertA1MSUser(sqLiteDatabase,a1MSUser4);

        A1MSUser a1MSUser5 = new A1MSUser();
        a1MSUser5.setEmail("smith@a1ms-uk.com");
        a1MSUser5.setMobile("01224 567 8905");
        a1MSUser5.setName("Smith");
        a1MSUser5.setEditable(true);
        dataSource.insertA1MSUser(sqLiteDatabase,a1MSUser5);

        A1MSUser a1MSUser6 = new A1MSUser();
        a1MSUser6.setEmail("steve@a1ms-uk.com");
        a1MSUser6.setMobile("01224 567 8906");
        a1MSUser6.setName("Steve");
        a1MSUser6.setEditable(true);
        dataSource.insertA1MSUser(sqLiteDatabase,a1MSUser6);

        A1MSUser a1MSUser7 = new A1MSUser();
        a1MSUser7.setEmail("test@a1ms-uk.com");
        a1MSUser7.setMobile("01224 567 8907");
        a1MSUser7.setName("Ashley");
        dataSource.insertA1MSUser(sqLiteDatabase,a1MSUser7);

        A1MSUser a1MSUser8 = new A1MSUser();
        a1MSUser8.setEmail("test@a1ms-uk.com");
        a1MSUser8.setMobile("01224 567 8901");
        a1MSUser8.setName("Ashley J");
        a1MSUser8.setEditable(true);
        dataSource.insertA1MSUser(sqLiteDatabase,a1MSUser8);

        A1MSUser a1MSUser9 = new A1MSUser();
        a1MSUser9.setEmail("test@a1ms-uk.com");
        a1MSUser9.setMobile("01224 567 8908");
        a1MSUser9.setName("James");
        a1MSUser9.setEditable(true);
        dataSource.insertA1MSUser(sqLiteDatabase,a1MSUser9);

        A1MSUser a1MSUser10 = new A1MSUser();
        a1MSUser10.setEmail("test1@a1ms-uk.com");
        a1MSUser10.setMobile("01224 567 8909");
        a1MSUser10.setName("Offspring");
        a1MSUser10.setEditable(true);
        dataSource.insertA1MSUser(sqLiteDatabase,a1MSUser10);


        A1MSUser a1MSUser11 = new A1MSUser();
        a1MSUser11.setEmail("test2@a1ms-uk.com");
        a1MSUser11.setMobile("01224 567 8911");
        a1MSUser11.setName("Star");
        a1MSUser11.setEditable(true);
        dataSource.insertA1MSUser(sqLiteDatabase,a1MSUser11);


        A1MSUser a1MSUser12 = new A1MSUser();
        a1MSUser12.setEmail("test3@a1ms-uk.com");
        a1MSUser12.setMobile("01224 567 8921");
        a1MSUser12.setName("Sun");
        a1MSUser12.setEditable(true);
        dataSource.insertA1MSUser(sqLiteDatabase,a1MSUser12);


        A1MSUser a1MSUser13 = new A1MSUser();
        a1MSUser13.setEmail("test4@a1ms-uk.com");
        a1MSUser13.setMobile("01224 567 8931");
        a1MSUser13.setName("Moon");
        a1MSUser13.setEditable(true);
        dataSource.insertA1MSUser(sqLiteDatabase,a1MSUser13);

        A1MSUser a1MSUser14 = new A1MSUser();
        a1MSUser14.setEmail("test5@a1ms-uk.com");
        a1MSUser14.setMobile("01224 567 8941");
        a1MSUser14.setName("Saturn");
        a1MSUser14.setEditable(true);
        dataSource.insertA1MSUser(sqLiteDatabase,a1MSUser14);

        A1MSUser a1MSUser15 = new A1MSUser();
        a1MSUser15.setEmail("test5@a1ms-uk.com");
        a1MSUser15.setMobile("01224 567 8951");
        a1MSUser15.setName("College Groups");
        a1MSUser15.setGroup(true);
        dataSource.insertA1MSUser(sqLiteDatabase,a1MSUser15);

        A1MSUser a1MSUser16 = new A1MSUser();
        a1MSUser16.setEmail("test5@a1ms-uk.com");
        a1MSUser16.setMobile("01224 567 8961");
        a1MSUser16.setName("Family");
        a1MSUser16.setGroup(true);
        dataSource.insertA1MSUser(sqLiteDatabase,a1MSUser16);
    }
}
