   M S B A M L   `   `   `  ����  DrawPolygons    DrawPolygons.MainWindow   5   	   \ XPresentationFramework, Version=4.0.0.0, Culture=neutral, PublicKeyToken=31bf3856ad364e35R NWindowsBase, Version=4.0.0.0, Culture=neutral, PublicKeyToken=31bf3856ad364e35W SPresentationCore, Version=4.0.0.0, Culture=neutral, PublicKeyToken=31bf3856ad364e35D 9http://schemas.microsoft.com/winfx/2006/xaml/presentation    5      R NSystem.Xaml, Version=4.0.0.0, Culture=neutral, PublicKeyToken=b77a5c561934e089:x,http://schemas.microsoft.com/winfx/2006/xaml     5      -   5        $��
mainWindow��5   2     � Title$  DrawPolygons��6   $	��400��6   $	��450��6&   .��5      �  � ColumnDefinitions 5      �� 5	      �� 5
      $
��Auto �6   .+�5      �� -   5        $��
drawCanvas��5      $��0��6!   
�������.+�5      � -   5        $��polyLine��5      
���   �$y�	Collapsed=�5      � 5      -   5        $��polygon��5      
���   �$y�	Collapsed=�61   � 5      -   5        $��filledPolygon��5      
����  �$y�	Collapsed=�5      5   	   �� 5      $��1��6    �� Orientation$ Vertical=�6#   
�������.+�5      �� $��Select Type��6   $��10q�6)   .��5      �� .+�5      	� -   5        $��	lineRadio��5      $��Polyline��6.   $��5q�5      6   	� 5"      -   5        $��polygonRadio��5"      $��Polygon��61   $��5q�5#      6   	� 5&      -   5        $��filledPolygonRadio��5&      $��Filled Polygon��5'      $��5q�6,   5(      5)      5*      �� 5-      -	   5        $��clearButton��5-      $��Clear��6%   $��5q�5.   '   5/   	   50      51      