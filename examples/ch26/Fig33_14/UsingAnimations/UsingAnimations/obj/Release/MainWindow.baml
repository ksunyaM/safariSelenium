   M S B A M L   `   `   `  ����   UsingAnimations     UsingAnimations.MainWindow   \ XPresentationFramework, Version=4.0.0.0, Culture=neutral, PublicKeyToken=31bf3856ad364e35R NWindowsBase, Version=4.0.0.0, Culture=neutral, PublicKeyToken=31bf3856ad364e35W SPresentationCore, Version=4.0.0.0, Culture=neutral, PublicKeyToken=31bf3856ad364e35D 9http://schemas.microsoft.com/winfx/2006/xaml/presentation    R NSystem.Xaml, Version=4.0.0.0, Culture=neutral, PublicKeyToken=b77a5c561934e089:x,http://schemas.microsoft.com/winfx/2006/xaml       � Title$  UsingAnimations��$	��400��$	��500��.���  � ColumnDefinitions �� �� $
��Auto �.+�r� -   $��video��$	��100��$	��100�� r� Stretch$ UniformToFill=� r� Source$ 	media.mp4?��� $��1��.+��� $
��Wrap=�$��Width:��$��5,0,0,0q��� -   $��
widthValue��$��75��$��5q�.��100�� $
��Wrap=�$��Height:��$��5,0,0,0q��� -   $��heightValue��$��75��$��5q�.��100�� $��Animate��$��75��$��5q� � Triggers :�  :� RoutedEvent$ Button.Click��.[��� .����  �� 
TargetName$ video��.��W�  g� Duration$ 0:0:2D� �� TargetProperty$ Width�		 W� To	 �� 
 �� ElementName$
 
widthValue�� �� Path$
 Text�W� $ 0:0:2D�$ Height�	 �� $
 heightValue��$
 Text�