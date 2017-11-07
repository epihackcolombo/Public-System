//
//  BreedingsiteHistory.m
//  mobuzz.ios.general
//
//  Created by Vajira on 3/9/15.
//  Copyright (c) 2015 cosmic. All rights reserved.
//

#import "BreedingsiteHistoryVC.h"
#import "GlobalVariableAndMethod.h"
#import "PopupMessages.h"
#import "BreedingsiteDetailsVC.h"
#import "ProfileLoginVC.h"

@interface BreedingsiteHistoryVC () <UITableViewDataSource, UITableViewDelegate>

@property (strong, nonatomic) IBOutlet UIButton *butnBack;
@property (strong, nonatomic) IBOutlet UIButton *butnSyncData;


@property NSArray *wardNameArray;
@property NSArray *complaintArray;

@property (strong, nonatomic) IBOutlet UIImageView *imgvComplaint;
@property (strong, nonatomic) IBOutlet UIActivityIndicatorView *actvUpdate;
@property (strong, nonatomic) IBOutlet UITableView *complaintTable;

@property UITableViewCell *cell;
@property BOOL refreshList;

@end


//Show complaint history details for user account
@implementation BreedingsiteHistoryVC

- (void)viewDidLoad {
    [super viewDidLoad];

    self.tabBarController.tabBar.hidden = YES;
    
    self.butnBack.layer.borderWidth = 1.0f;
    self.butnSyncData.layer.borderWidth = 1.0f;
    
    self.butnBack.layer.borderColor = [[GlobalVariableAndMethod getMobuzzColor:@"ash"]CGColor];
    self.butnSyncData.layer.borderColor = [[GlobalVariableAndMethod getMobuzzColor:@"yellow"]CGColor];
    
    
    [self.imgvComplaint setUserInteractionEnabled:YES];
    [self.imgvComplaint addGestureRecognizer:[[UITapGestureRecognizer alloc] initWithTarget:self action:@selector(tappedButton)]];

    self.wardNameArray = [kARRAY_WARD_NAME componentsSeparatedByString:@", "];
    
    self.complaintTable.estimatedRowHeight = 68.0;
    self.complaintTable.rowHeight = UITableViewAutomaticDimension;
    
    //To reload the list
    self.refreshList = YES;
}

-(void)viewDidAppear:(BOOL)animated {
    [super viewDidAppear:animated];
    
    if (self.refreshList) {
        [self checkInternetConnectionAndMakeLoadRequest];
    }
}

-(void)tappedButton {
    self.refreshList = YES; //To reload the list
    [self.tabBarController setSelectedIndex:0];
}


- (NSInteger)tableView:(UITableView *)tableView numberOfRowsInSection:(NSInteger)section {
    
    return self.complaintArray.count;
}


- (UITableViewCell *)tableView:(UITableView *)tableView cellForRowAtIndexPath:(NSIndexPath *)indexPath {

    self.cell = [tableView dequeueReusableCellWithIdentifier:@"ComplaintCell" forIndexPath:indexPath];
    
    NSDictionary *tmpDictionary = [self.complaintArray objectAtIndex:indexPath.row];
    
    //Continue populating the table if error occurs
    @try {
        
        UILabel *lablDateTime = (UILabel *)[self.cell viewWithTag:1001];
        UILabel *lablAddress = (UILabel *)[self.cell viewWithTag:1002];
        UILabel *lablWard = (UILabel *)[self.cell viewWithTag:1003];
        UILabel *lablMessage = (UILabel *)[self.cell viewWithTag:1004];
        UIImageView *imgvPhoto = (UIImageView *)[self.cell viewWithTag:1000];
        
        [lablDateTime setText:[tmpDictionary objectForKey:@"date"]];
        
        lablDateTime.text = [tmpDictionary objectForKey:@"date"];
        lablAddress.text = [tmpDictionary objectForKey:@"address"];
        lablMessage.text = [tmpDictionary objectForKey:@"cmcmessage"];
        
        int wardNo = [[tmpDictionary objectForKey:@"ward"] intValue];
        if (wardNo >= 0 && wardNo <= 47) {
            lablWard.text = [self.wardNameArray objectAtIndex:wardNo];
        } else {
            lablWard.text = [self.wardNameArray objectAtIndex:0];
        }
        
        int isImage = [[tmpDictionary objectForKey:@"image"] intValue];
        if (isImage>0) {
            imgvPhoto.image = [UIImage imageNamed:@"img_icon_download_enable.png"];
        } else {
            imgvPhoto.image = [UIImage imageNamed:@"img_icon_download_disable.png"];
        }

    }
    @catch (NSException * e) {
        //NSLog(@"Exception: %@", e);
    }

    return self.cell;
}


- (IBAction)backPressed:(UIButton *)sender {
    [self dismissViewControllerAnimated:YES completion:nil];
}


- (IBAction)syncDataPressed:(UIButton *)sender {
    
    [self checkInternetConnectionAndMakeLoadRequest];
}


#pragma mark - "make new complaint for breedingsite"

-(void)checkInternetConnectionAndMakeLoadRequest {
    
     [UIApplication sharedApplication].networkActivityIndicatorVisible = YES;
    
    //Check the internet connection
    NSURLSession *connTestSession = [NSURLSession sessionWithConfiguration:[NSURLSessionConfiguration defaultSessionConfiguration] delegate:self delegateQueue:[NSOperationQueue mainQueue] ];
    NSURLSessionDataTask * sessionDataTask = [connTestSession dataTaskWithURL:[NSURL URLWithString:@kCONN_URL_CHECK] completionHandler:^(NSData *data, NSURLResponse *response, NSError *error) {
        
         [UIApplication sharedApplication].networkActivityIndicatorVisible = NO;
        
        //Check connection is available or not
        if ((error == nil) && data) {
            
            //Create the request.
            [self callAsynPostComplaintHistory: [NSURL URLWithString:[NSString stringWithFormat:@"%@%@", @kCONN_URL_BASE, @kCONN_URL_COMPLAINT_HISTORY] ]];
        }
        else {
            
            //Popup message
            [self popdNetworkFailed:@kMSG_INTERNET_NO_TITLE1 message:@kMSG_INTERNET_NO_PARA1 button:@kMSG_COMMON_OK];
        }
        
            
    }];
    
    [sessionDataTask resume];
    
}


//Make a post call to server to login
-(void)callAsynPostComplaintHistory: (NSURL *)url {
    
    
    NSUserDefaults *userDefaults = [NSUserDefaults standardUserDefaults];
    
    NSString *username = [userDefaults stringForKey:@kLOGGED_USER_NAME];
    NSString *timeStamp = [userDefaults stringForKey:@kLOGGED_USER_TOKEN];
    NSString *uudid = [userDefaults stringForKey:@kLOGGED_USER_UUDID];

    NSError *errorJson;
    
    //Creating the json
    NSDictionary *jsonDictionary = [NSDictionary dictionaryWithObjectsAndKeys:

                                    username, @"user",
                                    timeStamp, @"time_stamp",
                                    uudid, @"uudid",
                                    nil];
    
    
    NSData *jsonData = [NSJSONSerialization dataWithJSONObject:jsonDictionary options:NSJSONWritingPrettyPrinted error:&errorJson];
    NSString *jsonString = [[NSString alloc] initWithData:jsonData encoding:NSUTF8StringEncoding];

    
    if (!errorJson) {
        NSData *postData=[jsonString dataUsingEncoding:NSUTF8StringEncoding];
        [self doProcessNetworkComplaintHistoryRequest:url data:postData flag:0];
    }
}


-(void)doProcessNetworkComplaintHistoryRequest:(NSURL *)url data:(NSData*)postData flag:(int)processFlag {
    
    NSMutableURLRequest *request = [NSMutableURLRequest requestWithURL:url];
    [request setHTTPMethod:@"POST"];
    [request addValue:@"application/json" forHTTPHeaderField:@"Content-Type"];
    [request setValue:[NSString stringWithFormat:@"%lu",(unsigned long)postData.length] forHTTPHeaderField:@"Content-Length"];
    [request setHTTPBody:postData];
    
    
    //Mark the start of network call
    self.actvUpdate.hidden = NO;
    self.view.userInteractionEnabled = NO;
    self.butnSyncData.userInteractionEnabled =NO;
    [self.actvUpdate startAnimating];
    
    NSURLSession *connSession = [NSURLSession sessionWithConfiguration:[NSURLSessionConfiguration defaultSessionConfiguration] delegate:nil delegateQueue:[NSOperationQueue mainQueue]];
    NSURLSessionDataTask *sessionPostDataTask = [connSession dataTaskWithRequest:request completionHandler:^(NSData * _Nullable data, NSURLResponse * _Nullable response, NSError * _Nullable connectionError) {

        //Mark the end of network call
        [self.actvUpdate stopAnimating];
        self.actvUpdate.hidden = YES;
        self.view.userInteractionEnabled = YES;
        self.butnSyncData.userInteractionEnabled = YES;
        
        
        if(data) {
            
            NSInteger responseCode = [(NSHTTPURLResponse *)response statusCode];
            NSString *strData = [[NSString alloc] initWithData:data encoding:NSASCIIStringEncoding];

            
            if (!connectionError && responseCode == 200) {
                
                [self doProcessUpdateResponse:strData flag:processFlag];
            }
            else {
                [self popdRequestFailed:@kMSG_REQUEST_FAIL_TITLE1 message:@kMSG_REQUEST_FAIL_PARA1 button:@kMSG_COMMON_OK];
            }
            
        }
        else {
            [self popdNetworkORServerFailed:@kMSG_CONNECT_NO_TITLE1 message:@kMSG_CONNECT_NO_PARA1 button:@kMSG_COMMON_OK];
        }
        
    }];
    [sessionPostDataTask resume];
    
}


//Process the response
-(void)doProcessUpdateResponse:(NSString *)reponseString flag:(int)processFlag {
    
    //validation block ------------------------
    //error_net_connection - no route to host, can't find an active connection
    //error_net_other - network time-out or other connector exceptions
    //error_db_params - inappropriate parameters with the request
    //error_db_connect - database connection issue at server side
    //authentication_required - user need to be authenticate
    //authentication_expired - user session has expired
    
    
    //Handle json data
    NSString *jsonString = [reponseString stringByReplacingOccurrencesOfString:@"'" withString:@"\""]; //Replacing character ' with " for json translation
    
    
    NSData *JSONdata = [jsonString dataUsingEncoding:NSUTF8StringEncoding];
    NSError *jsonError = nil;
    
    //Handle json data-part
    if (JSONdata != nil) {
        
        //If return response is an array
        self.complaintArray = [NSJSONSerialization JSONObjectWithData:JSONdata options:0 error:&jsonError];
    
        
        if ( (jsonError == nil) && (self.complaintArray) && ([jsonString rangeOfString:@"{\"status\"" options:NSCaseInsensitiveSearch].location == NSNotFound)) {
            
            if (self.complaintArray.count >= 1) {
                
                //If response is a array, populate the table
                
                NSMutableArray *mutableArray = [NSMutableArray arrayWithArray:[[self.complaintArray reverseObjectEnumerator] allObjects]];
                [mutableArray removeObjectAtIndex:0];
                self.complaintArray = [NSArray arrayWithArray:mutableArray];
                [self.complaintTable reloadData];
                
                dispatch_async(dispatch_get_main_queue(), ^{
                    //To refresh the table with new cell height
                    [self.complaintTable reloadData];
                });
                
                return;
            }
        }

        
        //If return response is not an array
        NSDictionary *dicJson = [NSJSONSerialization JSONObjectWithData:JSONdata options:0 error:&jsonError];

        if ( (jsonError != nil) || (dicJson == (NSDictionary *)[NSNull null]) ) {
            
            //It's a string response
            //NSLog(@"response error");
            [self popdRequestFailed:@kMSG_RESPONSE_UNEXPECTED_TITLE1 message:@kMSG_RESPONSE_UNEXPECTED_PARA1 button:@kMSG_COMMON_OK];
            
            return;
        }

        //Checking the values
        NSString *keyStatus = @"";
        
        if ([dicJson objectForKey:@"status"] != nil) {
            keyStatus = [dicJson objectForKey:@"status"];
        }
        
        
        if ([keyStatus caseInsensitiveCompare:@"error_db_params"] == NSOrderedSame ) {
            [self popdRequestFailed:@kMSG_RESPONSE_UNEXPECTED_TITLE2 message:@kMSG_RESPONSE_UNEXPECTED_PARA2 button:@kMSG_COMMON_OK];
            
        } else if ([keyStatus caseInsensitiveCompare:@"error_db_connect"] == NSOrderedSame ) {
            [self popdRequestFailed:@kMSG_RESPONSE_UNEXPECTED_TITLE2 message:@kMSG_RESPONSE_UNEXPECTED_PARA2 button:@kMSG_COMMON_OK];
            
        } else if ([keyStatus caseInsensitiveCompare:@"error_db_update"] == NSOrderedSame ) {
            [self popdRequestFailed:@kMSG_RESPONSE_UNEXPECTED_TITLE1 message:@kMSG_RESPONSE_UNEXPECTED_PARA1 button:@kMSG_COMMON_OK];
            
        } else if ([keyStatus caseInsensitiveCompare:@"authentication_failed"] == NSOrderedSame ) {
            [self popdRequestFailed:@kMSG_LOGINUSER_ERROR_TITLE1 message:@kMSG_LOGINUSER_ERROR_PARA1 button:@kMSG_COMMON_OK];
            
        } else if ([keyStatus caseInsensitiveCompare:@"authentication_expired"] == NSOrderedSame ) {
            //redirect to login page
            [self showPopupReAuthenticateDialog:@kMSG_RESPONSE_REAUTHENTICATION_TITLE2 message:@kMSG_RESPONSE_REAUTHENTICATION_PARA2 button:@kMSG_COMMON_OK];
            
        } else if ([keyStatus caseInsensitiveCompare:@"authentication_required"] == NSOrderedSame ) {
            //redirect to login page
            [self showPopupReAuthenticateDialog:@kMSG_RESPONSE_REAUTHENTICATION_TITLE1 message:@kMSG_RESPONSE_REAUTHENTICATION_PARA1 button:@kMSG_COMMON_OK];
            
        } else if ([keyStatus caseInsensitiveCompare:@"authentication_blocked"] == NSOrderedSame ) {
            //redirect to login page
            [self showPopupReAuthenticateDialog:@kMSG_RESPONSE_REAUTHENTICATION_TITLE3 message:@kMSG_RESPONSE_REAUTHENTICATION_PARA3 button:@kMSG_COMMON_OK];
            
        }
        else {
            //This block is for the undifine response
            [self popdRequestFailed:@kMSG_RESPONSE_UNEXPECTED_TITLE1 message:@kMSG_RESPONSE_UNEXPECTED_PARA1 button:@kMSG_COMMON_OK];
        }
        
    }
    else {
        //Error in getting json. Not the expected outcome.
        [self popdRequestFailed:@kMSG_RESPONSE_UNEXPECTED_TITLE1 message:@kMSG_RESPONSE_UNEXPECTED_PARA1 button:@kMSG_COMMON_OK];
        
    }
}



//--------- Popup messages ----------
-(void)showPopupDialog:(NSString *)byTitle message:(NSString *)byMessageText button:(NSString*)byButtonText {
    
    UIAlertController *alertController = [UIAlertController alertControllerWithTitle:byTitle message:byMessageText preferredStyle:UIAlertControllerStyleAlert];
    UIAlertAction *alertAction = [UIAlertAction actionWithTitle:byButtonText style:UIAlertActionStyleDefault handler:nil];
    
    [alertController addAction:alertAction];
    
    [self presentViewController:alertController animated:YES completion:nil];
}

-(void)popdNetworkFailed:(NSString *)byTitle message:(NSString *)byMessageText button:(NSString*)byButtonText {
    
    [self showPopupDialog:byTitle message:byMessageText button:byButtonText];
}

-(void)popdRequestFailed:(NSString *)byTitle message:(NSString *)byMessageText button:(NSString*)byButtonText {
    
    [self showPopupDialog:byTitle message:byMessageText button:byButtonText];
}

-(void)popdNetworkORServerFailed:(NSString *)byTitle message:(NSString *)byMessageText button:(NSString*)byButtonText {
    
    [self showPopupDialog:byTitle message:byMessageText button:byButtonText];
}

-(void)showPopupSuccessDialog:(NSString *)byTitle message:(NSString *)byMessageText button:(NSString*)byButtonText {
    
    UIAlertController *alertController = [UIAlertController alertControllerWithTitle:byTitle message:byMessageText preferredStyle:UIAlertControllerStyleAlert];
    UIAlertAction *alertAction = [UIAlertAction actionWithTitle:byButtonText style:UIAlertActionStyleDefault handler:^(UIAlertAction * action)
                                  {
                                      //Reset the UI
                                  }];
    
    [alertController addAction:alertAction];
    
    [self presentViewController:alertController animated:YES completion:nil];
}

-(void)showPopupReAuthenticateDialog:(NSString *)byTitle message:(NSString *)byMessageText button:(NSString*)byButtonText {
    
    UIAlertController *alertController = [UIAlertController alertControllerWithTitle:byTitle message:byMessageText preferredStyle:UIAlertControllerStyleAlert];
    UIAlertAction *alertAction = [UIAlertAction actionWithTitle:byButtonText style:UIAlertActionStyleDefault handler:^(UIAlertAction * action)
                                  {
                                      [self doLogoutUser];
                                  }];
    
    [alertController addAction:alertAction];
    
    [self presentViewController:alertController animated:YES completion:nil];
}


-(void)doLogoutUser {
    
    NSUserDefaults *userDefaults = [NSUserDefaults standardUserDefaults];
    
    //Set user defaults for this session
    [userDefaults setBool:NO forKey:@kIS_USER_LOGGED];
    [userDefaults setObject:@"" forKey:@kLOGGED_USER_NAME];
    [userDefaults setObject:@"" forKey:@kLOGGED_USER_TOKEN];
    [userDefaults setObject:@""forKey:@kLOGGED_USER_UUDID];
    [userDefaults setObject:@"" forKey:@kLOGGED_USER_LANGUAGE];
    [userDefaults synchronize];
    
    //[self performSegueWithIdentifier:@"unwindToProfileLoginVC" sender:self]; //Navigate to the login form.
    [self performSegueWithIdentifier:@"HistoryProfileLoginSegue" sender:nil];
}


-(void)prepareForSegue:(UIStoryboardSegue *)segue sender:(id)sender {
    
    if ([[segue identifier] isEqualToString:@"detailComplaintSegue"]) {
        
        self.refreshList = NO; //To stop reloading the list
        
        BreedingsiteDetailsVC *bdvc = segue.destinationViewController;
        
        UITableViewCell *cell = sender;
        NSIndexPath *indexPath = [self.complaintTable indexPathForCell:cell];
        bdvc.complaintDescription = [self.complaintArray objectAtIndex:indexPath.row];
        
        [self.complaintTable deselectRowAtIndexPath:indexPath animated:NO];
    }
    else if ([segue.identifier isEqualToString:@"HistoryProfileLoginSegue"]) {
        ProfileLoginVC *plvc = segue.destinationViewController;
        plvc.selfDismiss = YES;
    }

}

-(BOOL)shouldPerformSegueWithIdentifier:(NSString *)identifier sender:(id)sender {
    if([identifier isEqualToString:@"detailComplaintSegue"]) {
        return YES;
    }
    else {
        return NO;
    }
}




@end
