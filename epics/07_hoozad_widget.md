# 07 - e-commerce widget

## Description
As buyer I want to use the hoozad widget integrated in a e-commerce platform to send a product to a person from whom I do not know the delivery details

## Acceptance criteria

### 1 - As buyer I can use the hoozad widget with my facebook account

**Given** I'm in the delivery details selection of the e-commerce platform  
**And given** I have selected the hoozad widget  
**And given** The widget shows a button *Log-in with Facebook*  
**When** I click on the log-in button  
**Then** I can use my facebook log-in details  
**And then** A new user has been created in hoozad without delivery details or privacy restrictions  

### 2 - As buyer logged-in the widget, I see my facebook friends that have joined hoozad in receiver mode

**Given** I have a facebook connection *B* that has joined hoozad in receiver mode  
**When** I write "B" in the widget  
**Then** A reference to my connection *B* appears in the results section  
**And then** I can distinct that hoozad knows the delivery details of *B*  

### 3 - As buyer logged-in the widget, I see my facebook friends that have joined hoozad in sender mode

**Given** I have a facebook connection *B* that has joined hoozad in sender mode  
**When** I write "B" in the widget  
**Then** A reference to my connection *B* appears in the results section  
**And then** I can distinct that hoozad does not know the delivery details of *B*  

### 4 - As buyer logged-in the widget, I see my facebook friends that have not joined hoozad

**Given** I have a facebook connection *B* that has not joined hoozad
**When** I write "B" in the widget  
**Then** A reference to my connection *B* appears in the results section  
**And then** I can distinct that hoozad does not know the delivery details of *B*  

### 5 - As buyer logged-in the widget, I see non-facebook friends that have not joined hoozad 

### 6 - As buyer logged-in the widget, I see non-facebook friends that have joined hoozad without privacy restrictions


### 7 - As buyer logged-in the widget, I want to send an invite requests to users that have not joined hoozad

**Given** An existing user *B* has not joined hoozad  
**When** I select *B* in the hoozad widget  
**Then** Then I can send an invitation to join hoozad to *B*  

### 8 - As buyer logged-in the widget, I want to send a "complete profile" requests to users that have not added delivery details to hoozad

**Given** An existing user *B* has joined hoozad without delivery details  
**When** I select *B* in the hoozad widget  
**Then** Then I can send an invitation to complete the hoozad profile to *B*  

### 9 - As buyer logged-in the widget, I want to send a package to users with known delivery details in hoozad

**Given** An existing user *B* has joined hoozad with delivery details  
**When** I select *B* in the hoozad widget  
**Then** Then I can requests *B* delivery details for the e-commerce platform  