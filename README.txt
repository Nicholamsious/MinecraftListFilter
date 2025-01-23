# MinecraftListFilter: Generate a list of obtainable survival items
This program is designed to be used alongside [DecompilerMC](https://github.com/hube12/DecompilerMC).
This program generates a list of blocks and items which can only be obtained through Minecraft Survival or Hardcore mode.
Here's how to use it.
1. Open the command prompt (cmd).
2. Run `python main.py --mcv latest -q` or run `python main.py --mcv 1.14.4 -q` for a specific Minecraft version. This will generate `versions/1.14.4/client.jar` (with the version subfolder name depending on what version you specified).
3. Right click `versions/1.14.4/client.jar`, go to 7zip and click `Extract Here`.
4. When 7z consults you on whether to replace `version.json`, click `No` (this doesn't matter).
5. Copy the `assets/minecraft/items` folder. Paste `items` into the `MinecraftListFilter Input Files` folder. Note: this `items` folder doesn't appear in 1.14.4. It only appears in (version unknown!) and onwards.
6. Run MinecraftListFilter.
7. The (new or updated) file holding your filtered list of obtainable items in survival should now be in the `MinecraftListFilter Output File(s)` folder.
