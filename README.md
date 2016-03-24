# PokeMVVM
A playground for MVVM style architecture on Android

This architecture relies heavily on android databinding, but requires _nothing_ else not provided by the framework. You split up you app into the following components:

## Model
Holds are your data and buisness logic. You shouldn't need any of the android fragmework for this.

## View
Includes your layout files and any custom views you need to create. Have a very simple lifecycle of being created and destroyed on configuration changes. You use databinding to connect this to your view model.

## View Model
Includes are the logic to display your models and respond to user events. These have the same lifecycle of views.

## Api/Database
These obtain and store your data, often asynchrnously, but don't care about Android's lifecycle.

## Activity/Fragments
Used to coordinate the above components and deal with lifecycle events. May use loaders to get data from the api into the view model. Be careful with these, as soon as you need to do anything more complex than simple coordination, move it into it's own class.
