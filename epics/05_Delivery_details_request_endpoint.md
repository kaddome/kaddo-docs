# 05 - Delivery details request endpoint

## Description
As an e-commerce platform I want to requests delivery details, given a person identification details

## Acceptance criteria

### 1 - As accepted e-commerce platform I request delivery details given a known user-identifier (email)

**Given** an existing user *userA* with email address *userA@email.com* and delivery details *userA_delivery_details*
**When** an accepted e-commerce platform requests the delivery details for an user with email address *userA@email.com*
**Then** the response is a valid response containing the delivery details *userA_delivery_details*

### 2 - As accepted e-commerce platform I request delivery details for an unknown user-identifier (email)

**When** an accepted e-commerce platform requests the delivery details for an user with email address *unknownUser@email.com*
**Then** the response is a valid response containing the *processing request* response code
**And then** the app will send a sign-up request to *unknownUser@email.com* as defined in [epic#06 Sign-up request communications](06_Sign_up_request_communications.md)

### 3 - As non accepted e-commerce platform I request delivery details

**When** an non accepted e-commerce platform requests the delivery details of any user-identifier
**Then** the response returns an unauthorized error

