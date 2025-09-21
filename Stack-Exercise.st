!classDefinition: #OOStack category: #'Stack-Exercise'!
Object subclass: #OOStack
	instanceVariableNames: 'elements'
	classVariableNames: ''
	poolDictionaries: ''
	category: 'Stack-Exercise'!

!OOStack methodsFor: 'as yet unclassified' stamp: 'LM 9/19/2025 22:14:16'!
initialize
	elements := OrderedCollection new.! !

!OOStack methodsFor: 'as yet unclassified' stamp: 'LM 9/19/2025 22:17:35'!
isEmpty
	^(StackState new state: self) isEmpty.! !

!OOStack methodsFor: 'as yet unclassified' stamp: 'LM 9/19/2025 21:39:03'!
pop
	| poppedElement |
	self top.
	poppedElement := elements removeFirst.
	^poppedElement
	
	
! !

!OOStack methodsFor: 'as yet unclassified' stamp: 'LM 9/19/2025 22:16:42'!
push: anElement
	elements addFirst: anElement.! !

!OOStack methodsFor: 'as yet unclassified' stamp: 'LM 9/19/2025 21:23:56'!
size
	^elements size ! !

!OOStack methodsFor: 'as yet unclassified' stamp: 'LM 9/19/2025 22:17:42'!
top
	(StackState new state: self) top.
	^elements first.! !

"-- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- "!

!classDefinition: 'OOStack class' category: #'Stack-Exercise'!
OOStack class
	instanceVariableNames: ''!

!OOStack class methodsFor: 'error descriptions' stamp: 'NR 9/14/2023 08:12:21'!
stackEmptyErrorDescription
	
	^ 'stack is empty!!!!!!'! !


!classDefinition: #SentenceFinderByPrefix category: #'Stack-Exercise'!
Object subclass: #SentenceFinderByPrefix
	instanceVariableNames: ''
	classVariableNames: ''
	poolDictionaries: ''
	category: 'Stack-Exercise'!


!classDefinition: #StackState category: #'Stack-Exercise'!
Object subclass: #StackState
	instanceVariableNames: ''
	classVariableNames: ''
	poolDictionaries: ''
	category: 'Stack-Exercise'!

!StackState methodsFor: 'as yet unclassified' stamp: 'DC 9/21/2025 20:16:45'!
newState: aName compileMessages: aCollectionOfStatements
	|newSubclass|
	newSubclass := self class subclass: aName instanceVariableNames: '' classVariableNames: '' poolDictionaries: '' category: 'Stack-Exercise'.
	aCollectionOfStatements do: [:aStatement | newSubclass compile: aStatement].
	
	^newSubclass.! !

!StackState methodsFor: 'as yet unclassified' stamp: 'DC 9/21/2025 20:14:52'!
state: aStack
	|subclasses|
	subclasses := OrderedCollection new.
	subclasses add: (self  newState: #EmptyState compileMessages: {'isEmpty ^ true'. 'top ^self error: OOStack new class stackEmptyErrorDescription'. 'size: aStack ^ 0'}).
	subclasses add: (self  newState: #NonEmptyState compileMessages: {'isEmpty ^ false'. 'top ^self '. 'size: aStack ^ aStack size'.}).
	
	^ (subclasses detect: [:subclass | (subclass new size: aStack) = aStack size]) new.
	! !


!classDefinition: #EmptyState category: #'Stack-Exercise'!
StackState subclass: #EmptyState
	instanceVariableNames: ''
	classVariableNames: ''
	poolDictionaries: ''
	category: 'Stack-Exercise'!

!EmptyState methodsFor: 'as yet unclassified' stamp: 'DC 9/21/2025 20:17:14'!
isEmpty ^ true! !

!EmptyState methodsFor: 'as yet unclassified' stamp: 'DC 9/21/2025 20:17:14'!
size: aStack ^ 0! !

!EmptyState methodsFor: 'as yet unclassified' stamp: 'DC 9/21/2025 20:17:14'!
top ^self error: OOStack new class stackEmptyErrorDescription! !


!classDefinition: #NonEmptyState category: #'Stack-Exercise'!
StackState subclass: #NonEmptyState
	instanceVariableNames: ''
	classVariableNames: ''
	poolDictionaries: ''
	category: 'Stack-Exercise'!

!NonEmptyState methodsFor: 'as yet unclassified' stamp: 'DC 9/21/2025 20:17:14'!
isEmpty ^ false! !

!NonEmptyState methodsFor: 'as yet unclassified' stamp: 'DC 9/21/2025 20:17:14'!
size: aStack ^ aStack size! !

!NonEmptyState methodsFor: 'as yet unclassified' stamp: 'DC 9/21/2025 20:17:14'!
top ^self ! !


!classDefinition: #OOStackTest category: #'Stack-Exercise'!
TestCase subclass: #OOStackTest
	instanceVariableNames: ''
	classVariableNames: ''
	poolDictionaries: ''
	category: 'Stack-Exercise'!

!OOStackTest methodsFor: 'test' stamp: 'HernanWilkinson 5/7/2012 11:30'!
test01StackShouldBeEmptyWhenCreated

	| stack |
	
	stack := OOStack new.
	
	self assert: stack isEmpty! !

!OOStackTest methodsFor: 'test' stamp: 'NR 5/13/2020 13:29:55'!
test02PushAddElementsToTheStack

	| stack |
	
	stack := OOStack new.
	stack push: 'something'.
	
	self deny: stack isEmpty! !

!OOStackTest methodsFor: 'test' stamp: 'NR 5/13/2020 13:30:01'!
test03PopRemovesElementsFromTheStack

	| stack |
	
	stack := OOStack new.
	stack push: 'something'.
	stack pop.
	
	self assert: stack isEmpty! !

!OOStackTest methodsFor: 'test' stamp: 'NR 5/13/2020 13:30:09'!
test04PopReturnsLastPushedObject

	| stack pushedObject |
	
	stack := OOStack new.
	pushedObject := 'something'.
	stack push: pushedObject.
	
	self assert: stack pop = pushedObject! !

!OOStackTest methodsFor: 'test' stamp: 'NR 9/16/2021 17:40:17'!
test05StackBehavesLIFO

	| stack firstPushedObject secondPushedObject |
	
	stack := OOStack new.
	firstPushedObject := 'firstSomething'.
	secondPushedObject := 'secondSomething'.
	
	stack push: firstPushedObject.
	stack push: secondPushedObject.
	
	self assert: stack pop = secondPushedObject.
	self assert: stack pop = firstPushedObject.
	self assert: stack isEmpty 
	! !

!OOStackTest methodsFor: 'test' stamp: 'NR 5/13/2020 13:30:20'!
test06TopReturnsLastPushedObject

	| stack pushedObject |
	
	stack := OOStack new.
	pushedObject := 'something'.
	
	stack push: pushedObject.
	
	self assert: stack top = pushedObject.
	! !

!OOStackTest methodsFor: 'test' stamp: 'NR 5/13/2020 13:30:24'!
test07TopDoesNotRemoveObjectFromStack

	| stack pushedObject |
	
	stack := OOStack new.
	pushedObject := 'something'.
	
	stack push: pushedObject.
	
	self assert: stack size = 1.
	stack top.
	self assert: stack size = 1.
	! !

!OOStackTest methodsFor: 'test' stamp: 'HAW 4/14/2017 22:48:26'!
test08CanNotPopWhenThereAreNoObjectsInTheStack

	| stack  |
	
	stack := OOStack new.
	self
		should: [ stack pop ]
		raise: Error - MessageNotUnderstood 
		withExceptionDo: [ :anError |
			self assert: anError messageText = OOStack stackEmptyErrorDescription ]
		
! !

!OOStackTest methodsFor: 'test' stamp: 'NR 5/13/2020 13:30:31'!
test09CanNotPopWhenThereAreNoObjectsInTheStackAndTheStackHadObjects

	| stack  |
	
	stack := OOStack new.
	stack push: 'something'.
	stack pop.
	
	self
		should: [ stack pop ]
		raise: Error - MessageNotUnderstood 
		withExceptionDo: [ :anError |
			self assert: anError messageText = OOStack stackEmptyErrorDescription ]
		
! !

!OOStackTest methodsFor: 'test' stamp: 'HAW 4/14/2017 22:48:44'!
test10CanNotTopWhenThereAreNoObjectsInTheStack

	| stack  |
	
	stack := OOStack new.
	self
		should: [ stack top ]
		raise: Error - MessageNotUnderstood 
		withExceptionDo: [ :anError |
			self assert: anError messageText = OOStack stackEmptyErrorDescription ]
		
! !


!classDefinition: #SentenceFinderByPrefixTest category: #'Stack-Exercise'!
TestCase subclass: #SentenceFinderByPrefixTest
	instanceVariableNames: ''
	classVariableNames: ''
	poolDictionaries: ''
	category: 'Stack-Exercise'!
