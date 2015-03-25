# 05 - Delivery details request endpoint

## Description
As an e-commerce platform I want to requests delivery details, given a person identification details

## Acceptance criteria

### 1 - As accepted e-commerce platform I request delivery details given a known email for existing user

**Given** an existing user *userA* with email address *userA@email.com* and delivery details *userA_delivery_details*
**When** an accepted e-commerce platform requests the delivery details for an user with email address *userA@email.com*
**Then** the response is a valid response containing the expected delivery details


### 2 - As accepted e-commerce platform I request delivery details given an unknown email for existing user

### 3 - As accepted e-commerce platform I request delivery details for an unknown email for non-existing user


