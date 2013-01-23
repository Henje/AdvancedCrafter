git clone git://github.com/BuildCraft/BuildCraft.git
wget http://mcp.ocean-labs.de/files/mcp726a.zip
unzip mcp726a.zip
wget http://files.minecraftforge.net/minecraftforge/minecraftforge-src-latest.zip
unzip `ls | find minecraftforge*`
cd forge
chmod +x install.sh
./install.sh
cd ..
./recompile.sh
./reobfuscate.sh
cp texture reobf/minecraft/texture
cd reobf/minecraft
zip AdvancedCrafter.zip texture henje
mv AdvancedCrafter.zip ../../AdvancedCrafter_$VERSION.zip
