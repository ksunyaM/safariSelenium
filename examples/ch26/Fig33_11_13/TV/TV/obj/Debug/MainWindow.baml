   M S B A M L   `   `   `  ����  TV    TV.MainWindow   5   	   \ XPresentationFramework, Version=4.0.0.0, Culture=neutral, PublicKeyToken=31bf3856ad364e35R NWindowsBase, Version=4.0.0.0, Culture=neutral, PublicKeyToken=31bf3856ad364e35W SPresentationCore, Version=4.0.0.0, Culture=neutral, PublicKeyToken=31bf3856ad364e35D 9http://schemas.microsoft.com/winfx/2006/xaml/presentation    5      R NSystem.Xaml, Version=4.0.0.0, Culture=neutral, PublicKeyToken=b77a5c561934e089:x,http://schemas.microsoft.com/winfx/2006/xaml     5        � Title$  TV��5      $	��720��6   $	��720��6    � 	Resources 5         RadioButtonTemplate �� 
TargetType � Offset � Color �� Triggers P� Property P� Value �� 
TargetName	 �� Property
 �� Value��%f  &	        �� 5	      " 	�5
   
   .R�5      � .+�5      A� $��25��6   $��25��6!   
�������6-   A� 5      $��backgroundEllipse��6   $��22��6/   $��22��6:   ��5      
� 5      .��5      � $ 0Q�6$   $	 Red��6/   � 5      $
 1.25Q�6$   $ Black��62   5      5      �� 5      #��z��6   5       5      P� 5        � {� .  . �5!      �� $ backgroundEllipse��6   	 � ��
 5"      
� 5#      .��5$      � $ 0Q�6'   $ 	LightGray��62   � 5%      $
 1.25Q�6'   $ Black��65   5&      5(      5)      P� 5,       �@d�	IsChecked$
 TrueY�67   . �5-      �� $ backgroundEllipse��6   	 � ��
 5.      
� 5/      .��50      � $ 0Q�6'   $ 	LimeGreen��62   � 51      $
 1.25Q�6'   $ Black��65   52      54      55      57   	   58      .��5;      �� .+�5=      ��  �� Left$	 150��6   $	��370��6!   $	��490��6.   
 �� Top$ 20��5>   
   
���iii�._�5?      �  � RowDefinitions 5@      �� 5A      �� 5B      $
��Auto �6   .+�5F      �� $��	0,20,0,10q�6   
���   �$��Center=�5G      $��Center=�6-   $��2q�5H      
������� �� CornerRadius$ 2��69   ._�5I      r� -   5        $��videoMediaElement��5J      $	��300��5I      $	��400��6+    r� Source$ Video/media.mp4?�5J   ,    r� LoadedBehavior$ Manual=�5K       r� Stretch$
 Fill=�6+   5L      �� 5O      $��1��6   $��Right=�6&    �� Orientation$ 
Horizontal=�5P      .+�5Q      	� -   5        $��playRadioButton��5Q      {�.  $��0,0,5,15q�5R      #��[  5S      .��5U      �� $��20��6   $��20��6&   $��Images/play.png��5V      $��Uniform=�6/   5W      	� 5X      -   5        $��pauseRadioButton��5X      {�.  $��0,0,5,15q�5Y      #��[  5Z      .��5\      �� $��20��6   $��20��6&   $��Images/pause.png��5]      $��Uniform=�60   5^      	� 5_      -   5        $��stopRadioButton��5_      {�.  $��	0,0,15,15q�5`      #��[  5a      .��5c      �� $��20��6   $��20��6&   $��Images/stop.png��5d      $��Uniform=�6/   5e      5f      �� 5i      -   5        $��powerCheckBox��5i      $��1��6,   $��25��69   $��25��5j      $
��Left=�6   $��	15,0,0,15q�5k      ��5m      �� 5n      " ��6$   .R�5o      � .+�5q      A� $��25��6"   $��25��6-   
�������5r      A� 5u      $��backgroundEllipse��6"   $��22��6;   $��22��5v      ��5w      
� 5x       .��5y   #   � $ 0Q�60   $ 	LightGray��5z   %   � 5{   #   $
 1.25Q�60   $ Black��5|   %   5}   !   5      �� 5�      $��Images/power.png��6    $��20��6:   $��20��5�      5�       5�      P� 5�       �@d�	IsChecked$
 TrueY�5�      . �5�      �� $ backgroundEllipse��6$   	 � ��
 5�       
� 5�   #   .��5�   &   � $ 0Q�63   $ 	LimeGreen��5�   (   � 5�   &   $
 1.25Q�63   $ Black��5�   (   5�   $   5�      5�      5�      5�      5�      z�5�      �� 5�       �� AngleY$ 15Q�6    J� Effect 5�      3  -System.Windows.Media.Effects.DropShadowEffect  5�         Color$
 Gray��6      ShadowDepth$ 15Q�6,   5�   	   �� 5�      $	 185��6   $	 410��6!   $	��300��62   $	��400��5�   
   ._�5�      �� -   5        $��reflectionRectangle��5�      ��5�      !� 5�       !� Visual 5�      ��  �� ElementName$ videoMediaElement�� �� RelativeTransform 5�      �� 5�       �� ScaleY$ -1Q�6&    �� CenterY$	 0.5Q�62   5�       J� OpacityMask 5�      �� 5�       �� 
StartPoint$	 0,0&�6%    �� EndPoint$	 0,1&�66   .��5�      � $ Black��6!   $ -0.25Q�6/   � 5�      $ Transparent��6!   $	 0.5Q�65   5�      5�      z�5�      �� 5�      $ 15Q�6    �� AngleX$	 -45Q�6(   5�   	   5�      5�      