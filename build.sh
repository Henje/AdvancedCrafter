# git clone git://github.com/AdvancedCrafter/AdvancedCrafter.git
mkdir build
mv src build/src
mv texture build/texture
cd build
git clone git://github.com/BuildCraft/BuildCraft.git
wget http://mcp.ocean-labs.de/files/mcp726a.zip
unzip mcp726a.zip
wget http://files.minecraftforge.net/minecraftforge/minecraftforge-src-latest.zip
unzip minecraftforge-src-latest.zip
chmod +x .
python runtime/cleanup.py -f
cd forge
python install.py
cd ..
python runtime/recompile.py
python runtime/reobfuscate.py
mv texture reobf/minecraft/texture
cd reobf/minecraft
zip AdvancedCrafter.zip texture henje
mv AdvancedCrafter.zip ../../../AdvancedCrafter_$VERSION.zip
cd ../../../
rm -r -f build
