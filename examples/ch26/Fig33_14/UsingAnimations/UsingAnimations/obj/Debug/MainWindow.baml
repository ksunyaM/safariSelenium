   M S B A M L   `   `   `  ����  UsingAnimations     UsingAnimations.MainWindow   5   	   \ XPresentationFramework, Version=4.0.0.0, Culture=neutral, PublicKeyToken=31bf3856ad364e35R NWindowsBase, Version=4.0.0.0, Culture=neutral, PublicKeyToken=31bf3856ad364e35W SPresentationCore, Version=4.0.0.0, Culture=neutral, PublicKeyToken=31bf3856ad364e35D 9http://schemas.microsoft.com/winfx/2006/xaml/presentation    5      R NSystem.Xaml, Version=4.0.0.0, Culture=neutral, PublicKeyToken=b77a5c561934e089:x,http://schemas.microsoft.com/winfx/2006/xaml     5        � Title$  UsingAnimations��5      $	��400��6   $	��500��6)   .��5      �  � ColumnDefinitions 5      �� 5	      �� 5
      $
��Auto �6   .+�5      r� -   5        $��video��5      $	��100��6"   $	��100��6/    r� Stretch$ UniformToFill=�5   
    r� Source$ 	media.mp4?�6"   �� 5      $��1��6   .+�5      �� $
��Wrap=�6   $��Width:��6)   $��5,0,0,0q�67   �� 5      -   5        $��
widthValue��5      $��75��6%   $��5q�60   .��6;   1006@   �� 5      $
��Wrap=�6   $��Height:��6)   $��5,0,0,0q�68   �� 5      -   5        $��heightValue��5      $��75��6&   $��5q�61   .��6<   1006A   �� 5      $��Animate��6   $��75��6$   $��5q�6/    � Triggers 5      :� 5       :� RoutedEvent$ Button.Click��6   .[�5      �� .��5      ��  �� 
TargetName$ video��6"   .��5!      W�  g� Duration$ 0:0:2D�6*    �� TargetProperty$ Width�5"      		 W� To	 5#      �� 
 �� ElementName$
 
widthValue�� �� Path$
 Text�W� 5'      $ 0:0:2D�6*   $ Height�5(      	 5)      �� $
 heightValue��$
 Text�5+      5,      5-      5/      50   	   51      52      