#define AppName "Space Client"
#define AppVersion "0.1.0"
#define AppPublisher "Space Client"
#define AppExeName "Space Client.exe"

[Setup]
AppId={{A7B2E2C7-9A76-4B4D-8B68-5ACEC11E0001}
AppName={#AppName}
AppVersion={#AppVersion}
AppPublisher={#AppPublisher}
DefaultDirName={autopf}\Space Client
DefaultGroupName=Space Client
DisableProgramGroupPage=yes
OutputDir=..\build\installer
OutputBaseFilename=SpaceClient-Setup
Compression=lzma2
SolidCompression=yes
ArchitecturesInstallIn64BitMode=x64
SetupIconFile=..\build\space-client.ico
UninstallDisplayName=Space Client

[Tasks]
Name: "desktopicon"; Description: "Create a desktop icon"; GroupDescription: "Shortcuts:"; Flags: unchecked
Name: "startmenu"; Description: "Add Space Client to the Windows Start Menu/search"; GroupDescription: "Shortcuts:"; Flags: unchecked

[Files]
Source: "..\build\jpackage\Space Client\*"; DestDir: "{app}"; Flags: ignoreversion recursesubdirs createallsubdirs

[Icons]
Name: "{autodesktop}\Space Client"; Filename: "{app}\Space Client.exe"; Tasks: desktopicon
Name: "{group}\Space Client"; Filename: "{app}\Space Client.exe"; Tasks: startmenu

[Run]
Filename: "{app}\Space Client.exe"; Description: "Launch Space Client"; Flags: nowait postinstall skipifsilent
