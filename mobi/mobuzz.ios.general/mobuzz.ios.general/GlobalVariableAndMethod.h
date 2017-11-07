//
//  GlobalVariableAndMethod.h
//  mobuzz.ios.general
//
//  Created by Vajira on 27/7/15.
//  Copyright (c) 2015 cosmic. All rights reserved.
//

#import <Foundation/Foundation.h>
#import <CommonCrypto/CommonDigest.h>
#import <sys/utsname.h>
#import <UIKit/UIKit.h>

@interface GlobalVariableAndMethod : NSObject

+(NSString*)md5HexDigest:(NSString*)input;
+(NSString*)getCurrentDateTime:(NSString*)dateFormat;
+(NSString*)getDeviceName;
+(NSString *)getUUID;
+(BOOL)isNetworkAvailable;
+(BOOL)isUserTextValied:(NSString *)inputText;
+(UIColor *)getMobuzzColor: (NSString *)byName;
+(UIColor *)colorFromHexString:(NSString *)hexString;
+(BOOL)validateString:(NSString *)byString;


//Arrays
#define kARRAY_RESIDENCES @"Akarawita, Athurugiriya, Avissawella, Batawala, Battaramulla, Batugampola, Bope, Boralesgamuwa, Dedigamuwa, Dehiwala, Deltara, Habarakada, Handapangoda, Hanwella, Hewainna, Hiripitya, Hokandara, Homagama, Horagala, Kaduwela, Kahawala, Kalatuwawa, Kiriwattuduwa, Kolonnawa, Kosgama, Madapatha, Maharagama, Malabe, Meegoda, Moratuwa, Mount Lavinia, Mullegama, Mulleriyawa New Town, Mutwal, Napawela, Nugegoda, Padukka, Pannipitiya, Piliyandala, Pitipana Homagama, Polgasowita, Puwakpitiya, Ranala, Siddamulla, Sri Jayawardenepura, Talawatugoda, Tummodara, Waga, Watareka, Other"

#define kARRAY_LANGUAGES @"Sinhala, Tamil, English"
#define kARRAY_LANGUAGE_CODES @"si, ta-LK, en"

#define kARRAY_GENDER @"Male, Female"

#define kARRAY_WARD_NAME_LONG @"Not in CMC jurisdiction, Mattakkuliya (CMC ward no: 01), Modera (CMC ward no: 02), Mahawatte (CMC ward no: 03), Aluthmawatha (CMC ward no: 04), Lunupokuna (CMC ward no: 05), Bloemandhal (CMC ward no: 06), Kotahena East (CMC ward no: 07), Kotahena West (CMC ward no: 08), Kochchikade North (CMC ward no: 09), Gintupitiya (CMC ward no: 10), Masangas Weediya (CMC ward no: 11), New Bazaar (CMC ward no: 12), Grandpass North (CMC ward no: 13), Grandpass South (CMC ward no: 14), Maligawatte West (CMC ward no: 15), Aluthkade East (CMC ward no: 16), Aluthkade West (CMC ward no: 17), Kehelwatte (CMC ward no: 18), Kochchikade South (CMC ward no: 19), Fort (CMC ward no: 20), Kopannaweediya (CMC ward no: 21), Wekanda (CMC ward no: 22), Hunupitiya (CMC ward no: 23), Suduwella (CMC ward no: 24), Panchikawatte (CMC ward no: 25), Maradana (CMC ward no: 26), Maligakanda (CMC ward no: 27), Maligawatte East (CMC ward no: 28), Dematagoda (CMC ward no: 29), Wanathamulla (CMC ward no: 30), Kuppiyawatte East (CMC ward no: 31), Kuppiyawatte West (CMC ward no: 32), Borella North (CMC ward no: 33), Narahenpita (CMC ward no: 34), Borella South (CMC ward no: 35), Cinnamon Gardens (CMC ward no: 36), Kollupitiya (CMC ward no: 37), Bambalapitiya (CMC ward no: 38), Milagiriya (CMC ward no: 39), Thimbirigasyaya (CMC ward no: 40), Kirula (CMC ward no: 41), Havelok Twon (CMC ward no: 42), Wellewatte North (CMC ward no: 43), Kirillapone (CMC ward no: 44), Pamankada East (CMC ward no: 45), Pamankada West (CMC ward no: 46), Wellewatte South (CMC ward no: 47)"

#define kARRAY_WARD_NAME @"Not in CMC jurisdiction, Mattakkuliya (CMC ward), Modera (CMC ward), Mahawatte (CMC ward), Aluthmawatha (CMC ward), Lunupokuna (CMC ward), Bloemandhal (CMC ward), Kotahena East (CMC ward), Kotahena West (CMC ward), Kochchikade North (CMC ward), Gintupitiya (CMC ward), Masangas Weediya (CMC ward), New Bazaar (CMC ward), Grandpass North (CMC ward), Grandpass South (CMC ward), Maligawatte West (CMC ward), Aluthkade East (CMC ward), Aluthkade West (CMC ward), Kehelwatte (CMC ward), Kochchikade South (CMC ward), Fort (CMC ward), Kopannaweediya (CMC ward), Wekanda (CMC ward), Hunupitiya (CMC ward), Suduwella (CMC ward), Panchikawatte (CMC ward), Maradana (CMC ward), Maligakanda (CMC ward), Maligawatte East (CMC ward), Dematagoda (CMC ward), Wanathamulla (CMC ward), Kuppiyawatte East (CMC ward), Kuppiyawatte West (CMC ward), Borella North (CMC ward), Narahenpita (CMC ward), Borella South (CMC ward), Cinnamon Gardens (CMC ward), Kollupitiya (CMC ward), Bambalapitiya (CMC ward), Milagiriya (CMC ward), Thimbirigasyaya (CMC ward), Kirula (CMC ward), Havelok Twon (CMC ward), Wellewatte North (CMC ward), Kirillapone (CMC ward), Pamankada East (CMC ward), Pamankada West (CMC ward), Wellewatte South (CMC ward)"

//Url patterns
#define kCONN_URL_CHECK "http://www.google.com/m"
#define kCONN_URL_BASE  "http://202.129.235.206/mobuzz"
//#define kCONN_URL_BASE  "http://172.21.243.72/mobuzz"
#define kCONN_URL_PROFILE_LOGIN "/mob_profile_public_login.php"
#define kCONN_URL_PROFILE_REGISTER "/mob_profile_public_registration.php"
#define kCONN_URL_PROFILE_GET "/mob_profile_public_get_information.php"
#define kCONN_URL_PROFILE_EDIT "/mob_profile_public_edit_information.php"
#define kCONN_URL_PROFILE_RECOVER "/mob_profile_public_reset_password.php"
#define kCONN_URL_COMPLAINT_NEW "/mob_public_breedingsite_report_manage.php"
#define kCONN_URL_COMPLAINT_HISTORY "/mob_public_breedingsite_report_history.php"
#define kCONN_URL_COMPLAINT_DETAILS_IMAGE "/mob_public_breedingsite_report_image.php"
#define kCONN_URL_MAP "/mob_map_public_clusters_public.php"

#define kCOLOR_MOBUZZ_GRAY_HTML "#4F4F4F"

#define kDATE_TIME_FORMAT "YYYY-MM-dd HH:MM:ss"

#define kGPS_COLOMBO_LAT 6.911640
#define kGPS_COLOMBO_LNG 79.87

#define kIS_USER_LOGGED "IS_USER_LOGGED"

#define kKEYBOARD_MOVE_OFFSET 100

#define kLANGUAGE_DEFAULT "en"
#define kLOGGED_USER_NAME "LOGGED_USER_NAME"
#define kLOGGED_USER_TOKEN "LOGGED_USER_TOKEN"
#define kLOGGED_USER_LANGUAGE "LOGGED_USER_LANGUAGE"
#define kLOGGED_USER_UUDID "LOGGED_USER_UUDID"


#define kVALIDATE_MIN_TEXT_SIZE 5
#define kVALIDE_INPUT_VALUES_NAME @"abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ .:"
#define kVALIDE_INPUT_VALUES_EMAIL @"abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789 ,.:;?%@$*+-_#"
#define kVALIDE_INPUT_VALUES_USERNAME @"abcdefghijklmnopqrstuvwxyz0123456789.-_"
#define kVALIDE_INPUT_VALUES_NUMBERS @"0123456789"



    
@end
