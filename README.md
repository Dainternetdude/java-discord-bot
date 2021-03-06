# What is this?
This is a Discord bot written in Java that can link the chat of Minecraft servers with Discord channels.

# Why should I use this one instead of countless others?
Here's what makes this implementation unique:
- Compatible with vanilla (no mods or plugins of any kind required)
- Compatible with any Minecraft version
- Can be run remotely
- Beautiful graphical interface (WIP)
- Easy setup & configuration
- Supports multiple Minecraft servers
- Supports multiple Discord channels

# Features
- In-game scoreboard changing for non-operators using "[command prefix]score [scoreboard name]" (or "s" instead of "score")

# Planned features
- Emoji conversion
- Mention conversion
- Attachment support for Discord->Minecraft (currently attachments send as whitespace)
- Discord scoreboard commands

# Contributing
If you find an issue, report it in the issues tab.  
If you have a feature you'd like to see, post it in the issues tab.  
If you've written code you want to see in the program, feel free to make a pull request.  
If you think my code is bad, I already know about it. Make an issue if you like and I'll look into it if I'm not lazy.

# Instructions to build
1. Clone this repository
2. Run `gradlew shadowJar`
3. Look in `build/libs/`
