# 05 - Delivery details request endpoint

## Description
As an e-commerce platform I want to requests delivery details, given a person identification details

## Acceptance criteria

### [pilot] 1 - As an accepted e-commerce platform I request delivery details of a known user with delivery details

**Given** an existing hoozad user with facebook account *userA_fb* and delivery details *userA_delivery_details*  
**When** an accepted e-commerce platform requests the delivery details for a user with facebook account *userA_fb*  
**Then** the response is a valid response containing the delivery details *userA_delivery_details*  

### 2 - As an accepted e-commerce platform I request delivery details of a known user without delivery details

**Given** an existing hoozad user with facebook account *userA_fb* without delivery details  
**When** an accepted e-commerce platform requests the delivery details for a user with facebook account *userA_fb*  
**Then** the response is an error response "User without delivery details found"  

### [pilot] 3 - As accepted e-commerce platform I request delivery details for an unknown user

**Given** an unknown facebook user with account *userA_fb*  
**When** an accepted e-commerce platform requests the delivery details for an user with facebook account *userA_fb*  
**Then** the response is an error response "User not found"  

### 3 - As non accepted e-commerce platform I request delivery details

**When** an non accepted e-commerce platform requests the delivery details of any user-locator  
**Then** the response is an *unauthorized* error

### 4 - As accepted e-commerce platform I request delivery details for a nonexistent FB account

**When** an accepted e-commerce platform requests the delivery details for an user with FB account *nonexistentUser_fb*  
**Then** the response is a *not found* error
