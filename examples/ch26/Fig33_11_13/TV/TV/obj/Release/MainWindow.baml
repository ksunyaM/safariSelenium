   M S B A M L   `   `   `  ����   TV    TV.MainWindow   \ XPresentationFramework, Version=4.0.0.0, Culture=neutral, PublicKeyToken=31bf3856ad364e35R NWindowsBase, Version=4.0.0.0, Culture=neutral, PublicKeyToken=31bf3856ad364e35W SPresentationCore, Version=4.0.0.0, Culture=neutral, PublicKeyToken=31bf3856ad364e35D 9http://schemas.microsoft.com/winfx/2006/xaml/presentation    R NSystem.Xaml, Version=4.0.0.0, Culture=neutral, PublicKeyToken=b77a5c561934e089:x,http://schemas.microsoft.com/winfx/2006/xaml       � Title$  TV��$	��720��$	��720�� � 	Resources    RadioButtonTemplate �� 
TargetType � Offset � Color �� Triggers P� Property P� Value �� 
TargetName	 �� Property
 �� Value��%�  &	        �� " 	�.R�� .+�A� $��25��$��25��
�������A� $��backgroundEllipse��$��22��$��22����
� .��� $ 0Q�$	 Red��� $
 1.25Q�$ Black���� #��z�� P�  � {� .  . ��� $ backgroundEllipse��	 � ��
 
� .��� $ 0Q�$ 	LightGray��� $
 1.25Q�$ Black��P�  �@d�	IsChecked$
 TrueY�. ��� $ backgroundEllipse��	 � ��
 
� .��� $ 0Q�$ 	LimeGreen��� $
 1.25Q�$ Black��.���� .+���  �� Left$	 150��$	��370��$	��490��
 �� Top$ 20��
���iii�._��  � RowDefinitions �� �� $
��Auto �.+��� $��	0,20,0,10q�
���   �$��Center=�$��Center=�$��2q�
������� �� CornerRadius$ 2��._�r� -   $��videoMediaElement��$	��300��$	��400�� r� Source$ Video/media.mp4?� r� LoadedBehavior$ Manual=� r� Stretch$
 Fill=��� $��1��$��Right=� �� Orientation$ 
Horizontal=�.+�	� -   $��playRadioButton��{�.  $��0,0,5,15q�#��[  .���� $��20��$��20��$��Images/play.png��$��Uniform=�	� -   $��pauseRadioButton��{�.  $��0,0,5,15q�#��[  .���� $��20��$��20��$��Images/pause.png��$��Uniform=�	� -   $��stopRadioButton��{�.  $��	0,0,15,15q�#��[  .���� $��20��$��20��$��Images/stop.png��$��Uniform=��� -   $��powerCheckBox��$��1��$��25��$��25��$
��Left=�$��	15,0,0,15q����� " ��.R�� .+�A� $��25��$��25��
�������A� $��backgroundEllipse��$��22��$��22����
� .��� $ 0Q�$ 	LightGray��� $
 1.25Q�$ Black���� $��Images/power.png��$��20��$��20�� P�  �@d�	IsChecked$
 TrueY�. ��� $ backgroundEllipse��	 � ��
 
� .��� $ 0Q�$ 	LimeGreen��� $
 1.25Q�$ Black��z���  �� AngleY$ 15Q� J� Effect 3  -System.Windows.Media.Effects.DropShadowEffect     Color$
 Gray��   ShadowDepth$ 15Q��� $	 185��$	 410��$	��300��$	��400��._��� -   $��reflectionRectangle����!�  !� Visual ��  �� ElementName$ videoMediaElement�� �� RelativeTransform ��  �� ScaleY$ -1Q� �� CenterY$	 0.5Q� J� OpacityMask ��  �� 
StartPoint$	 0,0&� �� EndPoint$	 0,1&�.��� $ Black��$ -0.25Q�� $ Transparent��$	 0.5Q�z��� $ 15Q� �� AngleX$	 -45Q�