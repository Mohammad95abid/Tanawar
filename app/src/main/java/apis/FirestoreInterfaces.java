package apis;

import tanawar_objects.User;

public class FirestoreInterfaces {
    interface UsersCallback {
        void userExists(String title,String message);
        void userNotExists();
        void queryFailure(Exception e);
        void loginSuccessfully(User user);
        void loginFailure(String title,String message);
    }
}
