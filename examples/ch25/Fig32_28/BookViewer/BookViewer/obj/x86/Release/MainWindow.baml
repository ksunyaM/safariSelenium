   M S B A M L   `   `   `  ����   
BookViewer    BookViewer.MainWindow   \ XPresentationFramework, Version=4.0.0.0, Culture=neutral, PublicKeyToken=31bf3856ad364e35R NWindowsBase, Version=4.0.0.0, Culture=neutral, PublicKeyToken=31bf3856ad364e35W SPresentationCore, Version=4.0.0.0, Culture=neutral, PublicKeyToken=31bf3856ad364e35D 9http://schemas.microsoft.com/winfx/2006/xaml/presentation    R NSystem.Xaml, Version=4.0.0.0, Culture=neutral, PublicKeyToken=b77a5c561934e089:x,http://schemas.microsoft.com/winfx/2006/xaml     -   $��bookViewerWindow��  � Title$  Book Viewer��$	��400��$	��600�� � 	Resources    BookTemplate � ColumnDefinitions �� Path �� Orientation��%  &	        �� .R�� $	��250��$��3q� �� $
��Auto ��� .+��� $��0��$��50������ $ 
ThumbImage��� $��1��.+��� $	��3,0q�$
��Bold"�$
��Wrap=����� $ Title��� $	��3,0q�$ 
Horizontal=�.+��� $��ISBN: ���� ���� $
 ISBN�.���  �� $
��Auto ��� .+��� -    ��Name$ booksListView��$��0��#��[  �� $��1��$��5q�����  �� ElementName$ booksListView��$ SelectedItem.LargeImage�