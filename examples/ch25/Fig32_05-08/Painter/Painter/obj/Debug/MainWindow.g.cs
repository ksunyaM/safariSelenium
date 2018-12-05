﻿#pragma checksum "..\..\MainWindow.xaml" "{ff1816ec-aa5e-4d10-87f7-6f4963833460}" "35B28F3FF1090EE6BEE9246CF20BB70CDA1EBAA4"
//------------------------------------------------------------------------------
// <auto-generated>
//     This code was generated by a tool.
//     Runtime Version:4.0.30319.42000
//
//     Changes to this file may cause incorrect behavior and will be lost if
//     the code is regenerated.
// </auto-generated>
//------------------------------------------------------------------------------

using System;
using System.Diagnostics;
using System.Windows;
using System.Windows.Automation;
using System.Windows.Controls;
using System.Windows.Controls.Primitives;
using System.Windows.Data;
using System.Windows.Documents;
using System.Windows.Ink;
using System.Windows.Input;
using System.Windows.Markup;
using System.Windows.Media;
using System.Windows.Media.Animation;
using System.Windows.Media.Effects;
using System.Windows.Media.Imaging;
using System.Windows.Media.Media3D;
using System.Windows.Media.TextFormatting;
using System.Windows.Navigation;
using System.Windows.Shapes;
using System.Windows.Shell;


namespace Painter {
    
    
    /// <summary>
    /// MainWindow
    /// </summary>
    public partial class MainWindow : System.Windows.Window, System.Windows.Markup.IComponentConnector {
        
        
        #line 16 "..\..\MainWindow.xaml"
        [System.Diagnostics.CodeAnalysis.SuppressMessageAttribute("Microsoft.Performance", "CA1823:AvoidUnusedPrivateFields")]
        internal System.Windows.Controls.Canvas paintCanvas;
        
        #line default
        #line hidden
        
        
        #line 31 "..\..\MainWindow.xaml"
        [System.Diagnostics.CodeAnalysis.SuppressMessageAttribute("Microsoft.Performance", "CA1823:AvoidUnusedPrivateFields")]
        internal System.Windows.Controls.RadioButton redRadioButton;
        
        #line default
        #line hidden
        
        
        #line 33 "..\..\MainWindow.xaml"
        [System.Diagnostics.CodeAnalysis.SuppressMessageAttribute("Microsoft.Performance", "CA1823:AvoidUnusedPrivateFields")]
        internal System.Windows.Controls.RadioButton blueRadioButton;
        
        #line default
        #line hidden
        
        
        #line 35 "..\..\MainWindow.xaml"
        [System.Diagnostics.CodeAnalysis.SuppressMessageAttribute("Microsoft.Performance", "CA1823:AvoidUnusedPrivateFields")]
        internal System.Windows.Controls.RadioButton greenRadioButton;
        
        #line default
        #line hidden
        
        
        #line 37 "..\..\MainWindow.xaml"
        [System.Diagnostics.CodeAnalysis.SuppressMessageAttribute("Microsoft.Performance", "CA1823:AvoidUnusedPrivateFields")]
        internal System.Windows.Controls.RadioButton blackRadioButton;
        
        #line default
        #line hidden
        
        
        #line 47 "..\..\MainWindow.xaml"
        [System.Diagnostics.CodeAnalysis.SuppressMessageAttribute("Microsoft.Performance", "CA1823:AvoidUnusedPrivateFields")]
        internal System.Windows.Controls.RadioButton smallRadioButton;
        
        #line default
        #line hidden
        
        
        #line 49 "..\..\MainWindow.xaml"
        [System.Diagnostics.CodeAnalysis.SuppressMessageAttribute("Microsoft.Performance", "CA1823:AvoidUnusedPrivateFields")]
        internal System.Windows.Controls.RadioButton mediumRadioButton;
        
        #line default
        #line hidden
        
        
        #line 52 "..\..\MainWindow.xaml"
        [System.Diagnostics.CodeAnalysis.SuppressMessageAttribute("Microsoft.Performance", "CA1823:AvoidUnusedPrivateFields")]
        internal System.Windows.Controls.RadioButton largeRadioButton;
        
        #line default
        #line hidden
        
        
        #line 58 "..\..\MainWindow.xaml"
        [System.Diagnostics.CodeAnalysis.SuppressMessageAttribute("Microsoft.Performance", "CA1823:AvoidUnusedPrivateFields")]
        internal System.Windows.Controls.Button undoButton;
        
        #line default
        #line hidden
        
        
        #line 62 "..\..\MainWindow.xaml"
        [System.Diagnostics.CodeAnalysis.SuppressMessageAttribute("Microsoft.Performance", "CA1823:AvoidUnusedPrivateFields")]
        internal System.Windows.Controls.Button clearButton;
        
        #line default
        #line hidden
        
        private bool _contentLoaded;
        
        /// <summary>
        /// InitializeComponent
        /// </summary>
        [System.Diagnostics.DebuggerNonUserCodeAttribute()]
        [System.CodeDom.Compiler.GeneratedCodeAttribute("PresentationBuildTasks", "4.0.0.0")]
        public void InitializeComponent() {
            if (_contentLoaded) {
                return;
            }
            _contentLoaded = true;
            System.Uri resourceLocater = new System.Uri("/Painter;component/mainwindow.xaml", System.UriKind.Relative);
            
            #line 1 "..\..\MainWindow.xaml"
            System.Windows.Application.LoadComponent(this, resourceLocater);
            
            #line default
            #line hidden
        }
        
        [System.Diagnostics.DebuggerNonUserCodeAttribute()]
        [System.CodeDom.Compiler.GeneratedCodeAttribute("PresentationBuildTasks", "4.0.0.0")]
        [System.ComponentModel.EditorBrowsableAttribute(System.ComponentModel.EditorBrowsableState.Never)]
        [System.Diagnostics.CodeAnalysis.SuppressMessageAttribute("Microsoft.Design", "CA1033:InterfaceMethodsShouldBeCallableByChildTypes")]
        [System.Diagnostics.CodeAnalysis.SuppressMessageAttribute("Microsoft.Maintainability", "CA1502:AvoidExcessiveComplexity")]
        [System.Diagnostics.CodeAnalysis.SuppressMessageAttribute("Microsoft.Performance", "CA1800:DoNotCastUnnecessarily")]
        void System.Windows.Markup.IComponentConnector.Connect(int connectionId, object target) {
            switch (connectionId)
            {
            case 1:
            this.paintCanvas = ((System.Windows.Controls.Canvas)(target));
            
            #line 17 "..\..\MainWindow.xaml"
            this.paintCanvas.MouseMove += new System.Windows.Input.MouseEventHandler(this.paintCanvas_MouseMove);
            
            #line default
            #line hidden
            
            #line 18 "..\..\MainWindow.xaml"
            this.paintCanvas.MouseLeftButtonDown += new System.Windows.Input.MouseButtonEventHandler(this.paintCanvas_MouseLeftButtonDown);
            
            #line default
            #line hidden
            
            #line 19 "..\..\MainWindow.xaml"
            this.paintCanvas.MouseLeftButtonUp += new System.Windows.Input.MouseButtonEventHandler(this.paintCanvas_MouseLeftButtonUp);
            
            #line default
            #line hidden
            
            #line 20 "..\..\MainWindow.xaml"
            this.paintCanvas.MouseRightButtonDown += new System.Windows.Input.MouseButtonEventHandler(this.paintCanvas_MouseRightButtonDown);
            
            #line default
            #line hidden
            
            #line 21 "..\..\MainWindow.xaml"
            this.paintCanvas.MouseRightButtonUp += new System.Windows.Input.MouseButtonEventHandler(this.paintCanvas_MouseRightButtonUp);
            
            #line default
            #line hidden
            return;
            case 2:
            this.redRadioButton = ((System.Windows.Controls.RadioButton)(target));
            
            #line 32 "..\..\MainWindow.xaml"
            this.redRadioButton.Checked += new System.Windows.RoutedEventHandler(this.redRadioButton_Checked);
            
            #line default
            #line hidden
            return;
            case 3:
            this.blueRadioButton = ((System.Windows.Controls.RadioButton)(target));
            
            #line 34 "..\..\MainWindow.xaml"
            this.blueRadioButton.Checked += new System.Windows.RoutedEventHandler(this.blueRadioButton_Checked);
            
            #line default
            #line hidden
            return;
            case 4:
            this.greenRadioButton = ((System.Windows.Controls.RadioButton)(target));
            
            #line 36 "..\..\MainWindow.xaml"
            this.greenRadioButton.Checked += new System.Windows.RoutedEventHandler(this.greenRadioButton_Checked);
            
            #line default
            #line hidden
            return;
            case 5:
            this.blackRadioButton = ((System.Windows.Controls.RadioButton)(target));
            
            #line 39 "..\..\MainWindow.xaml"
            this.blackRadioButton.Checked += new System.Windows.RoutedEventHandler(this.blackRadioButton_Checked);
            
            #line default
            #line hidden
            return;
            case 6:
            this.smallRadioButton = ((System.Windows.Controls.RadioButton)(target));
            
            #line 48 "..\..\MainWindow.xaml"
            this.smallRadioButton.Checked += new System.Windows.RoutedEventHandler(this.smallRadioButton_Checked);
            
            #line default
            #line hidden
            return;
            case 7:
            this.mediumRadioButton = ((System.Windows.Controls.RadioButton)(target));
            
            #line 50 "..\..\MainWindow.xaml"
            this.mediumRadioButton.Checked += new System.Windows.RoutedEventHandler(this.mediumRadioButton_Checked);
            
            #line default
            #line hidden
            return;
            case 8:
            this.largeRadioButton = ((System.Windows.Controls.RadioButton)(target));
            
            #line 53 "..\..\MainWindow.xaml"
            this.largeRadioButton.Checked += new System.Windows.RoutedEventHandler(this.largeRadioButton_Checked);
            
            #line default
            #line hidden
            return;
            case 9:
            this.undoButton = ((System.Windows.Controls.Button)(target));
            
            #line 59 "..\..\MainWindow.xaml"
            this.undoButton.Click += new System.Windows.RoutedEventHandler(this.undoButton_Click);
            
            #line default
            #line hidden
            return;
            case 10:
            this.clearButton = ((System.Windows.Controls.Button)(target));
            
            #line 63 "..\..\MainWindow.xaml"
            this.clearButton.Click += new System.Windows.RoutedEventHandler(this.clearButton_Click);
            
            #line default
            #line hidden
            return;
            }
            this._contentLoaded = true;
        }
    }
}

