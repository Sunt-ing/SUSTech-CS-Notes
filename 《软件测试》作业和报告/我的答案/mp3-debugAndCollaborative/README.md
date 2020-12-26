# MP3

**Name**: 孙挺

**Student ID**: 11710108

**Selected App**: [AnkiDroid](https://github.com/ankidroid/Anki-Android)



## Part 1

### a

#### Issue 1: [Undo: NoSuchElementException - LinkedBlockingDeque.removeLast ](https://github.com/ankidroid/Anki-Android/issues/7776)

##### i.  

> Select similar closed issue from your App  

- [Crash on double-tap of undo in reviewer](https://github.com/ankidroid/Anki-Android/issues/5152)

> Why is this closed issue selected? 

- Because there is also "`NoSuchElementException`" message in its crash message, which means that it's also an issue about `NoSuchElementException`.

> What are the keywords that you used to search for this closed issue?  

- `NoSuchElementException`

##### ii.

> Could you find the buggy location (the buggy lines/method/UI component that cause the error/bug) based on the selected closed
> issue in your selected app in A? If yes, explain why do you think that this location is buggy.  If not, explain the reason (you could use the stack trace to find the location for a crash)  

- Yes.  
- Although the closed one was closed just because no one can reproduce it rather than it's really confirmed and fixed, the root of the one is found out. 
- The real root of the open one is that there is no such a mechanism as a good debouncing mechanism in the application, so when the "undo" button is double clicked, the behavior is undefined. Therefore, if we want to solve it, it seems that we have to discuss the right behavior of double-tap of "undo" button, which can be deploy some highly-completed framework in the application to prevent double-tap of "undo" button from destroying the application.

##### iii.

> Could you fix the open issue based on the selected closed issue in your selected app in A? If not, explain the reason.  

- Yes.  
- We can add some highly-completed framework into the application to solve it. But in this case, we need to select and discuss about it carefully. 
- And adding a new framework takes a additional cost, which makes it doubtful whether it is the best solution for the application.



#### Issue 2: [Note: Inconsistent Schema Assertion failure](https://github.com/ankidroid/Anki-Android/issues/7747)

##### i.  

> Select similar closed issue from your App  

- [Avoid production crash for short stack traces](https://github.com/ankidroid/Anki-Android/pull/5209)

> Why is this closed issue selected? 

- Because it's also an issue about failure of inconsistency.

> What are the keywords that you used to search for this closed issue?  

- `Assertion failure`

##### ii.

> Could you find the buggy location (the buggy lines/method/UI component that cause the error/bug) based on the selected closed
> issue in your selected app in A? If yes, explain why do you think that this location is buggy. If not, explain the reason (you could use the stack trace to find the location for a crash)  

- No. `"Assertion failure"` as both of they meet, they are a little different. 
- The closed one just choose not to throw an exception when something bad really happen. 
- However, the open one is hard to reproduce or dig out, which means that they are not strongly related. 

##### iii.

> Could you fix the open issue based on the selected closed issue in your selected app in A? If not, explain the reason.  

- No. 
- Because as mentioned in last sub-question, it's still remains to be found where is the root of the crash. 
- After the root of the crash is found, it will be relatively easier to solve the issue.



#### Issue 3: [Crash 2.14.0 IndexOutOfBoundsException in NoteEditor.removeButton](https://github.com/ankidroid/Anki-Android/issues/7724)

##### i.  

> Select similar closed issue from your App  

- [SuggestionsPopupWindow - IndexOutOfBoundsException: setSpan (9 ... 18) ends beyond length 9](https://github.com/ankidroid/Anki-Android/issues/6170)

> Why is this closed issue selected? 

- Because it's also an issue about failure of `IndexOutOfBoundsException`.

> What are the keywords that you used to search for this closed issue?  

- `IndexOutOfBoundsException`

##### ii.

> Could you find the buggy location (the buggy lines/method/UI component that cause the error/bug) based on the selected closed
> issue in your selected app in A? If yes, explain why do you think that this location is buggy. If not, explain the reason (you could use the stack trace to find the location for a crash)  

- Yes.  
- There are 2 cases of `IndexOutOfBoundsException`. 
  - The first one is that your index is smaller than 0, which is impossible to get.
  - And the other one is that your index is equal to or bigger than the iterator' length. 
- Therefore, I find that it is the quick double-tap of the `NoteEditor.removeButton` that rise the problem.

##### iii.

> Could you fix the open issue based on the selected closed issue in your selected app in A? If not, explain the reason.  

- Yes.
- As we mentioned, it is because of the quick double-tap of the `NoteEditor.removeButton`that cause the problem. 
- Therefore, we can add some quick double-tap protections in the application to prevent issues of the quick double-tap from being resulted again.



#### Issue 4: [Crash: TagsDialog not attached to a context](https://github.com/ankidroid/Anki-Android/issues/7263)

##### i.  

> Select similar closed issue from your App  

- [IllegalStateException crash in media recording](https://github.com/ankidroid/Anki-Android/pull/5002)

> Why is this closed issue selected? 

- Because it's also an issue about failure of `java.lang.IllegalStateException`.

> What are the keywords that you used to search for this closed issue?  

- `IllegalStateException`

##### ii.

> Could you find the buggy location (the buggy lines/method/UI component that cause the error/bug) based on the selected closed
> issue in your selected app in A? If yes, explain why do you think that this location is buggy. If not, explain the reason (you could use the stack trace to find the location for a crash)  

- No. `"java.lang.IllegalStateException"` as both of they meet, they are a little different. 
- The closed one was occurred under media recording circumstances, while the open one under deck preview circumstance. 
- Yet, I found that fragment `TagsDialog` not attached to a context, which may be one of the roots of the crash.

##### iii.

> Could you fix the open issue based on the selected closed issue in your selected app in A? If not, explain the reason.  

- No. Because as mentioned in last sub-question, it's still remains to be found where is the root of the crash. After the root of the crash is found, it will be relatively easier to solve the issue.



#### Issue 5: [Decks.confForDid crash](https://github.com/ankidroid/Anki-Android/issues/6880)

##### i.  

> Select similar closed issue from your App  

- [Avoid production crash for short stack traces](https://github.com/ankidroid/Anki-Android/pull/5209)

> Why is this closed issue selected? 

- Because it also an issue about failure of `java.lang.NullPointerException`.

> What are the keywords that you used to search for this closed issue?  

- `java.lang.NullPointerException`

##### ii.

> Could you find the buggy location (the buggy lines/method/UI component that cause the error/bug) based on the selected closed
> issue in your selected app in A? If yes, explain why do you think that this location is buggy. If not, explain the reason (you could use the stack trace to find the location for a crash)  

- Yes.
- The buggy location of the open one is that there is a deck which has a configuration id and the configuration itself does not exists. 
- For instance, the configuration C was set to a deck D, but C was deleted. Then sync lead to a deck with a config which does not exists anymore.

##### iii.

> Could you fix the open issue based on the selected closed issue in your selected app in A? If not, explain the reason.  

- Yes.  
- To completely solve the issue, we need to find out some related codes in the application, trying to understand the deep mechanisms of the application.



### b

#### Issue 1: [Crash showing options menu in CardBrowser](https://github.com/ankidroid/Anki-Android/issues/6875)

##### i. Similar issue: [Crash while choosing category](https://github.com/federicoiosue/Omni-Notes/issues/741)

> How is the closed issue selected? 

- I search for issues from a similar note-taking application [Omni-Notes](https://github.com/federicoiosue/Omni-Notes)

##### ii.

> Why is this closed issue selected? 

- Because it's also an issue about failure of `java.lang.NullPointerException`.

> What are the keywords that you used to search for this closed issue?  

- `NullPointerException`

##### iii.

> Could you find the buggy location (the buggy lines/method/UI component that cause the error/bug) based on the selected closed issue in your selected app in A? If yes, explain why do you think that this location is buggy. If not, explain the reason (you could use the stack trace to find the location for a crash).  

- Yes.
- By track the issue information it lists, I find that the methods are called out of order. 
- Therefore, `mActionBarTitle` and `mCardsAdapter` turn null, which result in the `NullPointerException`.



#### Issue 2: [[2.9 ACRA] crash in Collection.addNote - null DB reference?](https://github.com/ankidroid/Anki-Android/issues/5524)

##### i. Similar issue: [Crash on "cancel last modifications" on new note](https://github.com/federicoiosue/Omni-Notes/issues/704)

> How is the closed issue selected? 

- I search for issues from similar apps [Omni-Notes](https://github.com/federicoiosue/Omni-Notes)

##### ii.

> Why is this closed issue selected? 

- Because it also an issue about failure of null DB reference.

> What are the keywords that you used to search for this closed issue?  

- `null DB reference`

##### iii.

> Could you find the buggy location (the buggy lines/method/UI component that cause the error/bug) based on the selected closed issue in your selected app in A? If yes, explain why do you think that this location is buggy. If not, explain the reason (you could use the stack trace to find the location for a crash).  

- No. 
- Both issues lack descriptions. 
- The open one just shows the stack trace, and the closed one has no pull request link or detailed solution description. Most importantly, both of them have no reproduce procedure, making them almost impossible to reproduce.



#### Issue 3: [Audio recording crashes](https://github.com/ankidroid/Anki-Android/issues/5069)

##### i. Similar issue: [DetailFragment state non persistent on rotation](https://github.com/federicoiosue/Omni-Notes/issues/278)

> How is the closed issue selected? 

- I search for issues from similar apps [Omni-Notes](https://github.com/federicoiosue/Omni-Notes)

##### ii.

> Why is this closed issue selected? 

- Because it's also an issue about failure of `Audio recording` and the [Omni-Notes](https://github.com/federicoiosue/Omni-Notes) is also a note-taking application.

> What are the keywords that you used to search for this closed issue?  

- `Audio recording` 

##### iii.

> Could you find the buggy location (the buggy lines/method/UI component that cause the error/bug) based on the selected closed issue in your selected app in A? If yes, explain why do you think that this location is buggy. If not, explain the reason (you could use the stack trace to find the location for a crash).  

- No. 
- I try to reproduce the issue, but find that this issue cannot be reproduced in all environments.
- I ran the program at real phones everything is OK, and error did not appear. 
- Started to test at virtual devices and I met a exception: `java.lang.RuntimeException: stop failed`
- It seems that some thing really weird happens.



#### Issue 4: [Camera doesn't work when "Don't Keep Activities" is set](https://github.com/ankidroid/Anki-Android/issues/5863)

##### i. Similar issue: [Switch camera doesn't work with the android](https://github.com/twilio/twilio-video.js/issues/1307)

> How is the closed issue selected? 

- I search for some keywords in GitHub

##### ii.

> Why is this closed issue selected? 

- Because it's also an issue about failure of `camera doesn't work`, and both of them are Android application.

> What are the keywords that you used to search for this closed issue?  

- `camera doesn't work`. The actual URL I inputted is: `https://github.com/search?q=Camera+doesn%27t+work&state=closed&type=Issues`

##### iii.

> Could you find the buggy location (the buggy lines/method/UI component that cause the error/bug) based on the selected closed issue in your selected app in A? If yes, explain why do you think that this location is buggy. If not, explain the reason (you could use the stack trace to find the location for a crash).  

- No. 
- `camera doesn't work` as both of they meet, they are totally different. 
- Since "camera" is somewhat a hardware-related component, it's a little bit more complicated for software-man to solve. 
- Yet, when I try to get some solutions in stackoverflow.com, I find that the issue can be split into several issues. So I have to find out the roots of them one by one. 



#### Issue 5: [Anki doesn't show color's name](https://github.com/ankidroid/Anki-Android/issues/5648)

##### i. Similar issue: [Tighten color regex](https://github.com/samuelcolvin/pydantic/pull/2155)

> How is the closed issue selected? 

- I search for some keywords in GitHub

##### ii.

> Why is this closed issue selected? 

- Because it's also an issue about color and regex.

> What are the keywords that you used to search for this closed issue?  

- `color regex`

##### iii.

> Could you find the buggy location (the buggy lines/method/UI component that cause the error/bug) based on the selected closed issue in your selected app in A? If yes, explain why do you think that this location is buggy. If not, explain the reason (you could use the stack trace to find the location for a crash).  

- Yes. 
- When I reproduce it, I find that it happens only with night mode turned on. Then I find that night mode has a specific color inversion system that this must be tripping into. Something about regex needs a change. Search the code for "invert" then I find these regex-related code snippets.
- Maybe an online regex tool can be used to propose a fix.



## Part 2

> a) The method in Question 1a is more efficient (time taken) than the method in Question 1b.  

My choice is `iv. Agree`.



> b) The method in Question 1a is more effective (likelihood of finding a relevant fix) than the method Question 1b.  

My choice is `iv. Agree`.



> c) Compared to referring to similar issues (using methods in Question 1a and Question 1b), I think that manually finding and fixing bugs directly is more efficient (time taken).  

My choice is `ii. Disagree`.



> d) Compared to referring to similar issues (using methods in Question 1a and Question 1b), I think that manually finding and fixing bugs directly is more effective (likelihood of finding a fix).  

My choice is `ii. Disagree`.



> e) Explain your answers in a)-d) by including either (1) the number of bugs successfully fixed with a method to support your claim, or (2) a suggestion to improve the methods (1a or 1b) above.

- Since different applications differ dramatically, an issue usually is not a good reference for issues from another application, even the applications are similar. 
- But the similar issues in the same application help. When we try to find out the root of some issues, if there are similar closed issues, sometimes we can get some insights from them.

- For example, in the first issue of part 1a, Issue 3:  [Crash 2.14.0 IndexOutOfBoundsException in NoteEditor.removeButton](https://github.com/ankidroid/Anki-Android/issues/7724), the similar issue [SuggestionsPopupWindow - IndexOutOfBoundsException: setSpan (9 ... 18) ends beyond length 9](https://github.com/ankidroid/Anki-Android/issues/6170) from the same application shows that double-tap can easily result in some exception, like `NullPointerException`. Therefore, this open issue can also be resulted from double-tap. And it does.

  