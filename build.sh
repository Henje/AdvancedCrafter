# git clone git://github.com/AdvancedCrafter/AdvancedCrafter.git
mkdir build
cp src build/src
cp texture build/texture
0cd build
git clone git://github.com/BuildCraft/BuildCraft.git
wget http://mcp.ocean-labs.de/files/mcp726a.zip
unzip mcp726a.zip
wget http://files.minecraftforge.net/minecraftforge/minecraftforge-src-latest.zip
unzip minecraftforge-src-latest.zip
chmod +x .
cd forge
./install.sh
cd ..
./recompile.sh
./reobfuscate.sh
cp texture reobf/minecraft/texture
cd reobf/minecraft
zip AdvancedCrafter.zip texture henje
mv AdvancedCrafter.zip ../../../AdvancedCrafter_$VERSION.zip
cd ../../../
rm -r build
