!classDefinition: #CustomerBook category: #'CodigoRepetido-Ejercicio'!
Object subclass: #CustomerBook
	instanceVariableNames: 'suspended active'
	classVariableNames: ''
	poolDictionaries: ''
	category: 'CodigoRepetido-Ejercicio'!

!CustomerBook methodsFor: 'testing' stamp: 'NR 4/3/2019 10:14:26'!
includesCustomerNamed: aName

	^(active includes: aName) or: [ suspended includes: aName ]! !

!CustomerBook methodsFor: 'testing' stamp: 'NR 4/3/2019 10:14:26'!
isEmpty
	
	^active isEmpty and: [ suspended isEmpty ]! !


!CustomerBook methodsFor: 'initialization' stamp: 'NR 9/17/2020 07:23:04'!
initialize

	active := OrderedCollection new.
	suspended:= OrderedCollection new.! !


!CustomerBook methodsFor: 'customer management' stamp: 'DC 9/10/2025 00:33:44'!
addCustomerNamed: aName

	aName isEmpty ifTrue: [ self signalCustomerNameCannotBeEmpty ].
	(self includesCustomerNamed: aName) ifTrue: [ self signalCustomerAlreadyExists ].
	
	active add: aName ! !

!CustomerBook methodsFor: 'customer management' stamp: 'NR 4/3/2019 10:14:26'!
numberOfActiveCustomers
	
	^active size! !

!CustomerBook methodsFor: 'customer management' stamp: 'NR 4/3/2019 10:14:26'!
numberOfCustomers
	
	^active size + suspended size! !

!CustomerBook methodsFor: 'customer management' stamp: 'NR 9/19/2018 17:36:09'!
numberOfSuspendedCustomers
	
	^suspended size! !

!CustomerBook methodsFor: 'customer management' stamp: 'DC 9/10/2025 23:49:30'!
removeCustomer: aName IfFoundin: aCategory
	"comment stating purpose of message"

	1 to: aCategory size do: [:customer |
		aName = (aCategory at: customer)
			ifTrue: [
				aCategory removeAt: customer.
				^ aName] 
		].! !

!CustomerBook methodsFor: 'customer management' stamp: 'DC 9/10/2025 23:49:49'!
removeCustomerNamed: aName 
 
	(self removeCustomer: aName IfFoundin: active ) = aName ifTrue: [^ aName].
	(self removeCustomer: aName IfFoundin: suspended) = aName ifTrue: [^ aName].
	
	^ NotFound signal.
! !

!CustomerBook methodsFor: 'customer management' stamp: 'HernanWilkinson 7/6/2011 17:52'!
signalCustomerAlreadyExists 

	self error: self class customerAlreadyExistsErrorMessage! !

!CustomerBook methodsFor: 'customer management' stamp: 'HernanWilkinson 7/6/2011 17:51'!
signalCustomerNameCannotBeEmpty 

	self error: self class customerCanNotBeEmptyErrorMessage ! !

!CustomerBook methodsFor: 'customer management' stamp: 'NR 4/3/2019 10:14:26'!
suspendCustomerNamed: aName 
	
	(active includes: aName) ifFalse: [^CantSuspend signal].
	
	active remove: aName.
	
	suspended add: aName
! !

"-- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- "!

!classDefinition: 'CustomerBook class' category: #'CodigoRepetido-Ejercicio'!
CustomerBook class
	instanceVariableNames: ''!

!CustomerBook class methodsFor: 'error messages' stamp: 'NR 4/9/2023 22:25:52'!
customerAlreadyExistsErrorMessage

	^'Customer already exists!!!!!!'! !

!CustomerBook class methodsFor: 'error messages' stamp: 'NR 4/9/2023 22:25:56'!
customerCanNotBeEmptyErrorMessage

	^'Customer name cannot be empty!!!!!!'! !


!classDefinition: #CantSuspend category: #'CodigoRepetido-Ejercicio'!
Error subclass: #CantSuspend
	instanceVariableNames: ''
	classVariableNames: ''
	poolDictionaries: ''
	category: 'CodigoRepetido-Ejercicio'!


!classDefinition: #NotFound category: #'CodigoRepetido-Ejercicio'!
Error subclass: #NotFound
	instanceVariableNames: ''
	classVariableNames: ''
	poolDictionaries: ''
	category: 'CodigoRepetido-Ejercicio'!


!classDefinition: #CustomerBookTest category: #'CodigoRepetido-Ejercicio'!
TestCase subclass: #CustomerBookTest
	instanceVariableNames: ''
	classVariableNames: ''
	poolDictionaries: ''
	category: 'CodigoRepetido-Ejercicio'!

!CustomerBookTest methodsFor: 'testing' stamp: 'DC 9/10/2025 23:37:59'!
assertThatBlock: aBlock runsInMilliseconds: anAmountOfMilliseconds
	"Checks if the given block runs in given amount of milliseconds"

	| millisecondsBeforeRunning millisecondsAfterRunning |
	
	millisecondsBeforeRunning := Time millisecondClockValue * millisecond.
	aBlock value.
	millisecondsAfterRunning := Time millisecondClockValue * millisecond.
	self assert: (millisecondsAfterRunning-millisecondsBeforeRunning) < (anAmountOfMilliseconds * millisecond). ! !

!CustomerBookTest methodsFor: 'testing' stamp: 'rc 9/11/2025 19:23:17'!
createCustomerBookWithOneCustomer: aName
	"creates a customer book with one customer named as the aName argument"

	| customerBook |
	
	customerBook := CustomerBook new.
	customerBook addCustomerNamed: aName.
	
	^customerBook! !

!CustomerBookTest methodsFor: 'testing' stamp: 'DC 9/11/2025 01:54:49'!
send: aMessage toCustomerBook: aCustomerBook withCollaborator: aCollaborator on: anException firstAssert: firstAssert thenAssert: lastAssert
	"comment stating purpose of message"

	[ aCustomerBook perform: aMessage with: aCollaborator . self fail ]
		on: anException 
		do: [ :anError | 
			self assert: (firstAssert value: anError).
			self assert: (lastAssert value: anError)]
! !

!CustomerBookTest methodsFor: 'testing' stamp: 'DC 9/10/2025 01:14:08'!
test01AddingCustomerShouldNotTakeMoreThan50Milliseconds

	| customerBook|
	
	customerBook := CustomerBook new.
	
	self assertThatBlock: [customerBook addCustomerNamed: 'John Lennon'] runsInMilliseconds: 50
	
	
! !

!CustomerBookTest methodsFor: 'testing' stamp: 'rc 9/11/2025 19:23:58'!
test02RemovingCustomerShouldNotTakeMoreThan100Milliseconds

	| customerBook paulMcCartney |
	
	paulMcCartney := 'Paul McCartney'.
	customerBook := self createCustomerBookWithOneCustomer: paulMcCartney.
	
	self assertThatBlock: [customerBook removeCustomerNamed: paulMcCartney] runsInMilliseconds: 100
	
! !

!CustomerBookTest methodsFor: 'testing' stamp: 'DC 9/11/2025 01:55:45'!
test03CanNotAddACustomerWithEmptyName 

	| customerBook |
			
	customerBook := CustomerBook new.
	
	self send: #addCustomerNamed: toCustomerBook: customerBook withCollaborator: '' on: Error 
	firstAssert: [:anError | anError messageText = CustomerBook customerCanNotBeEmptyErrorMessage] 
	thenAssert: [:anError | customerBook isEmpty].

	! !

!CustomerBookTest methodsFor: 'testing' stamp: 'rc 9/11/2025 19:25:55'!
test04CanNotRemoveAnInvalidCustomer
	
	| customerBook johnLennon |
			
	johnLennon := 'John Lennon'.
	customerBook := self createCustomerBookWithOneCustomer: johnLennon.
	
	self send: #removeCustomerNamed: toCustomerBook: customerBook withCollaborator: 'Paul McCartney' 
	on: NotFound 
	firstAssert: [:anError | customerBook numberOfCustomers = 1] 
	thenAssert: [:anError | customerBook includesCustomerNamed: johnLennon].

! !

!CustomerBookTest methodsFor: 'testing' stamp: 'rc 9/11/2025 19:26:40'!
test05SuspendingACustomerShouldNotRemoveItFromCustomerBook

	| customerBook paulMcCartney|
	
	paulMcCartney := 'Paul McCartney'.
	customerBook := self createCustomerBookWithOneCustomer: paulMcCartney.
	
	customerBook suspendCustomerNamed: paulMcCartney.
	
	self validate: customerBook hasActives: 0 hasSuspended: 1 hasTotal: 1 check: #assert: customerExists: paulMcCartney.
	

	
! !

!CustomerBookTest methodsFor: 'testing' stamp: 'rc 9/11/2025 19:27:39'!
test06RemovingASuspendedCustomerShouldRemoveItFromCustomerBook

	| customerBook paulMcCartney|
	
	paulMcCartney := 'Paul McCartney'.
	customerBook := self createCustomerBookWithOneCustomer: paulMcCartney.

	customerBook suspendCustomerNamed: paulMcCartney.
	customerBook removeCustomerNamed: paulMcCartney.
	
	self validate: customerBook hasActives: 0 hasSuspended: 0 hasTotal: 0 check: #deny: customerExists: paulMcCartney.


	
! !

!CustomerBookTest methodsFor: 'testing' stamp: 'rc 9/11/2025 19:28:21'!
test07CanNotSuspendAnInvalidCustomer
	
	| customerBook johnLennon |
		
	johnLennon := 'John Lennon'.
	customerBook := self createCustomerBookWithOneCustomer: johnLennon.
	
	self send: #suspendCustomerNamed: toCustomerBook: customerBook withCollaborator: 'George Harrison' 
	on: CantSuspend 
	firstAssert: [:anError | customerBook numberOfCustomers = 1]
	thenAssert: [:anError | customerBook includesCustomerNamed: johnLennon].
! !

!CustomerBookTest methodsFor: 'testing' stamp: 'rc 9/11/2025 19:29:08'!
test08CanNotSuspendAnAlreadySuspendedCustomer
	
	| customerBook johnLennon |
			
	johnLennon := 'John Lennon'.
	customerBook := self createCustomerBookWithOneCustomer: johnLennon.
	
	customerBook suspendCustomerNamed: johnLennon.
	
	self send: #suspendCustomerNamed: toCustomerBook: customerBook withCollaborator: johnLennon
	on: CantSuspend 
	firstAssert: [:anError | customerBook numberOfCustomers = 1]
	thenAssert: [:anError | customerBook includesCustomerNamed: johnLennon].
	
! !

!CustomerBookTest methodsFor: 'testing' stamp: 'DC 9/11/2025 01:24:38'!
validate: aCustomerBook hasActives: numberOfActives hasSuspended: numberOfSuspended hasTotal: numberOfCustomers check: aCheck customerExists: aName
	"comment stating purpose of message"

	self assert: numberOfActives equals: aCustomerBook numberOfActiveCustomers.
	self assert: numberOfSuspended equals: aCustomerBook numberOfSuspendedCustomers.
	self assert: numberOfCustomers equals: aCustomerBook numberOfCustomers.
	self perform: aCheck with: (aCustomerBook includesCustomerNamed: aName).! !
