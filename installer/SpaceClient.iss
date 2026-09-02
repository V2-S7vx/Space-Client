#define MyAppName "Space Client"
#define MyAppVersion "0.1.0"
#define MyAppPublisher "Space Client"
#define MyAppExeName "SpaceClient.exe"

[Setup]
AppId={{C5B3E3C6-0E4A-4E62-9E0D-SPACECLIENT01}
AppName={#MyAppName}
AppVersion={#MyAppVersion}
AppPublisher={#MyAppPublisher}
DefaultDirName={localappdata}\Programs\Space Client
DefaultGroupName={#MyAppName}
DisableProgramGroupPage=no
PrivilegesRequired=lowest
OutputDir=build\installer
OutputBaseFilename=SpaceClient-Setup
SetupIconFile=assets\SpaceClient.ico
UninstallDisplayIcon={app}\{#MyAppExeName}
Compression=lzma2
SolidCompression=yes
WizardStyle=modern
ArchitecturesInstallIn64BitMode=x64

[Tasks]
Name: "desktopicon"; Description: "Create a desktop shortcut"; GroupDescription: "Shortcuts:"
Name: "startmenu"; Description: "Create a Start Menu shortcut"; GroupDescription: "Shortcuts:"; Flags: checkedonce

[Files]
Source: "build\launcher-image\SpaceClient\*"; DestDir: "{app}"; Flags: ignoreversion recursesubdirs createallsubdirs

[Icons]
Name: "{group}\Space Client"; Filename: "{app}\{#MyAppExeName}"; Tasks: startmenu
Name: "{autodesktop}\Space Client"; Filename: "{app}\{#MyAppExeName}"; Tasks: desktopicon

[Run]
Filename: "{app}\{#MyAppExeName}"; Description: "Launch Space Client"; Flags: nowait postinstall skipifsilent
