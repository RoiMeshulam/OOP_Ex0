# Author
Roi Meshulam, contact via mail: rohimesh21@gmail.com.

# What is this project?

Lets say you have a building with elevators, how do you make all the elevators work in the most efficient way possible?

This project offers 1 solution.

We work in a "Leveling" method -> That means that we each time check a few conditions and each condition is a different way to handle the problem

In fact inside my project you will find a Building,CallForElevator and Elevator interfaces that were written by @benmoshe and that will be used to check the algorithem.

Inside the "algo" folder you will find 4 algorithems.

3 of them "ShabatElevator" are "simple" elevators that are getting allocated in the simplest way.

The "SmartElevator" is my algorithem and it contains 2 important methods:

**allocateTo** -> Gets a call and decides which elevator should handle it.

**cmdElevator** -> The main method that "refreshes" the elevators and "updates" them where to move and what to do

# How to use our project

To use this project you will need to run "Ex0_main" that was written by @benmoshe with examples from https://github.com/benmoshe/OOP_2021/tree/main/Assignments/Ex0/libs

Thank you for reading, hope you find good usage of it :)
