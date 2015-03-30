# 05 - Delivery details request endpoint

## Description
As an e-commerce platform I want to requests delivery details, given a person identification details

## Acceptance criteria

### 1 - As an accepted e-commerce platform I request delivery details given a known FB account

**Given** an existing user *userA* with FB account *userA_fb* and delivery details *userA_delivery_details*  
**When** an accepted e-commerce platform requests the delivery details for a user with email address *userA_fb*  
**Then** the response is a valid response containing the delivery details *userA_delivery_details*  

### 2 - As accepted e-commerce platform I request delivery details for an unknown FB account

**When** an accepted e-commerce platform requests the delivery details for an user with FB account *unknownUser_fb*  
**Then** the response is a valid response containing the *processing request* response code  
**And then** the app will send a sign-up request to *unknownUser_fb* as defined in [epic#06 Sign-up request communications](06_Sign_up_request_communications.md)  

### 3 - As non accepted e-commerce platform I request delivery details

**When** an non accepted e-commerce platform requests the delivery details of any user-locator  
**Then** the response is an *unauthorized* error

### 4 - As accepted e-commerce platform I request delivery details for a nonexistent FB account

**When** an accepted e-commerce platform requests the delivery details for an user with FB account *nonexistentUser_fb*  
**Then** the response is a *not found* error