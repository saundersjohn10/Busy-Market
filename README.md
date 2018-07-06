# Busy-Market

This project is supposed to simulate a busy farmer's market.  There are customers in lanes who are all trying to access the fruit.  However, the fruit is only sold one at a time.  The project utilizes Concurrency and Locks.  

Video showing the program running:
https://drive.google.com/file/d/1bKpmk-f-TwO2F1Jd2gGvBZ1M2qXIrlES/view?usp=sharing

The project is visually displayed by printing out the market to the console every time a change is made to the market.  The customers slowing work there way to the fruit in line.  There are 6 threads running the project: 3 adding people to each respective lane and 3 allowing a customer to buy fruit.  There exists one lock that each thread is fighting over, and when a lock is given up, the lock is randomly given to another thread trying to get the lock, allowing it to perform its respective function. 

An example of when there is 58 fruit left:

<img width="144" alt="screen shot 2018-07-06 at 10 56 13 am" src="https://user-images.githubusercontent.com/36249204/42386019-5a4f3f54-810c-11e8-9109-efb95bab7461.png">

The program runs until there are no fruit left:

<img width="143" alt="screen shot 2018-07-06 at 10 54 04 am" src="https://user-images.githubusercontent.com/36249204/42386043-6cf229be-810c-11e8-9cbe-069f6cca3e55.png">

A customer object is defined that is randomly assigned a letter.  The toString method of it is edited to display the letter when called.

The lanes utilizes a Linked List containing the customers which allowed me to keep track of the ordering of when a customer gets in line.  A queue would have been great to keep track of this, but the Linked List would allowed me to recurse through it when it is printed.  When a customer is removed, the head is deleted and the next node becomes the head.  
