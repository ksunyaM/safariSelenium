   M S B A M L   `   `   `  ���� 	  Clock    Clock.MainWindow   \ XPresentationFramework, Version=4.0.0.0, Culture=neutral, PublicKeyToken=31bf3856ad364e35R NWindowsBase, Version=4.0.0.0, Culture=neutral, PublicKeyToken=31bf3856ad364e35W SPresentationCore, Version=4.0.0.0, Culture=neutral, PublicKeyToken=31bf3856ad364e35D 9http://schemas.microsoft.com/winfx/2006/xaml/presentation    R NSystem.Xaml, Version=4.0.0.0, Culture=neutral, PublicKeyToken=b77a5c561934e089:x,http://schemas.microsoft.com/winfx/2006/xaml     -   $��clockWindow��  � Title$  Clock��$	��118��$	��118�� � WindowStyle$
 None=� � AllowsTransparency .  � 	Resources    ButtonTemplate �� 
TargetType �� CornerRadius �� Triggers P� Property P� Value	 �� 
TargetName
 �� Property �� Value��%�   &	        �� " ��.R��� $��Border��$��2q�$ 2��
����iA�._��� $��0q�$��8��#��z�� P�  �@J�IsMouseOver . . ��� $	 Border��
 � ��
 ��ح�����  �� ImageSource$ images/circle.png��.���  � RowDefinitions �� $
��Auto ��� .+��� $��0�� �� Orientation$ 
Horizontal=�$��Right=�.+��� -    ��Name$ minimizeButton��$��0q�|�.  ��.  #��[  .���� $��images/minimize.png��$��0q��� -   $ closeButton��$��1,0,0,0q�|�.  ��.  #��[  .���� $��images/close.png��$��0q��� -    ��Name$ timeTextBox��$��1��$��0,30,0,0q�
������  �� TextAlignment$ Center=�$
��Bold"�
�������$��16(�$��0q� � Cursor$ Arrow��|�.  