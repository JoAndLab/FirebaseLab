package du.joandlab.firebaselab;

import java.util.Random;

/**
 * Created by gson73 on 2016-10-28.
 */

public class AvatarPicker {

    private static Random randomAvatarGenerator = new Random();
    private static int NUMBER_OF_AVATAR = 3;


    /*Generate an avatar randomly*/
    public static int generateRandomAvatarForUser() {
        return randomAvatarGenerator.nextInt(NUMBER_OF_AVATAR);
    }

    /*Get avatar id*/

    public static int getDrawableAvatarId(int givenRandomAvatarId) {

        switch (givenRandomAvatarId) {

            case 0:
                return R.drawable.abra;
            case 1:
                return R.drawable.bellsprout;
            case 2:
                return R.drawable.caterpie;
            default:
                return R.drawable.abra;
        }
    }


}
