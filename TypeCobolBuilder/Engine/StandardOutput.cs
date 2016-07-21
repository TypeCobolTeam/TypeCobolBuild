using System;
using System.Collections.Generic;
using System.IO;
using System.Linq;
using System.Text;
using System.Threading.Tasks;

namespace TypeCobolBuilder.Engine
{
    /// <summary>
    /// This class is used only to redirect standard output in debugging mode so that it
    /// can be possible to track where a message is output.
    /// </summary>
    public class StandardOutput : TextWriter
    {
        TextWriter m_txtWritter;
        public StandardOutput(TextWriter txtWritter)
        {
            m_txtWritter = txtWritter;
        }

        public override IFormatProvider FormatProvider 
        {
            get
            {
                return m_txtWritter.FormatProvider;
            }
        }
        public override string NewLine 
        {
            get
            {
                return m_txtWritter.NewLine;
            }
            set
            {
                m_txtWritter.NewLine = value;
            }
        }
        public override void Flush()
        {
            m_txtWritter.Flush();
        }
        public override Task FlushAsync()
        {
            return m_txtWritter.FlushAsync();
        }

        public override void Write(bool value)
        {
            m_txtWritter.Write(value);
        }
        public override void Write(char value)
        {
            m_txtWritter.Write(value);
        }
        public override void Write(char[] buffer)
        {
            m_txtWritter.Write(buffer);
        }
        public override void Write(decimal value)
        {
            m_txtWritter.Write(value);
        }
        public override void Write(double value)
        {
            m_txtWritter.Write(value);
        }
        public override void Write(float value)
        {
            m_txtWritter.Write(value);
        }
        public override void Write(int value)
        {
            m_txtWritter.Write(value);
        }
        public override void Write(long value)
        {
            m_txtWritter.Write(value);
        }
        public override void Write(object value)
        {
            m_txtWritter.Write(value);
        }
        public override void Write(string value)
        {
            m_txtWritter.Write(value);
        }
        public override void Write(uint value)
        {
            m_txtWritter.Write(value);
        }
        public override void Write(ulong value)
        {
            m_txtWritter.Write(value);
        }
        public override void Write(string format, object arg0)
        {
            m_txtWritter.Write(format, arg0);
        }
        public override void Write(string format, params object[] arg)
        {
            m_txtWritter.Write(format, arg);
        }
        public override void Write(char[] buffer, int index, int count)
        {
            m_txtWritter.Write(buffer, index, count);
        }
        public override void Write(string format, object arg0, object arg1)
        {
            m_txtWritter.Write(format, arg0, arg1);
        }
        public override void Write(string format, object arg0, object arg1, object arg2)
        {
            m_txtWritter.Write(format, arg0, arg1, arg2);
        }
        public override Task WriteAsync(char value)
        {
            return m_txtWritter.WriteAsync(value);
        }
        public new Task WriteAsync(char[] buffer)
        {
            return m_txtWritter.WriteAsync(buffer);
        }
        public override Task WriteAsync(string value)
        {
            return m_txtWritter.WriteAsync(value);
        }
        public override Task WriteAsync(char[] buffer, int index, int count)
        {
            return m_txtWritter.WriteAsync(buffer, index, count);
        }
        public override void WriteLine()
        {
            m_txtWritter.WriteLine();
        }
        public override void WriteLine(bool value)
        {
            m_txtWritter.WriteLine(value);
        }
        public override void WriteLine(char value)
        {
            m_txtWritter.WriteLine(value);
        }
        public override void WriteLine(char[] buffer)
        {
            m_txtWritter.WriteLine(buffer);
        }
        public override void WriteLine(decimal value)
        {
            m_txtWritter.WriteLine(value);
        }
        public override void WriteLine(double value)
        {
            m_txtWritter.WriteLine(value);
        }
        public override void WriteLine(float value)
        {
            m_txtWritter.WriteLine(value);
        }
        public override void WriteLine(int value)
        {
            m_txtWritter.WriteLine(value);
        }
        public override void WriteLine(long value)
        {
            m_txtWritter.WriteLine(value);
        }
        public override void WriteLine(object value)
        {
            m_txtWritter.WriteLine(value);
        }
        public override void WriteLine(string value)
        {
            m_txtWritter.WriteLine(value);
        }
        public override void WriteLine(uint value)
        {
            m_txtWritter.WriteLine(value);
        }
        public override void WriteLine(ulong value)
        {
            m_txtWritter.WriteLine(value);
        }
        public override void WriteLine(string format, object arg0)
        {
            m_txtWritter.WriteLine(format, arg0);
        }
        public override void WriteLine(string format, params object[] arg)
        {
            m_txtWritter.WriteLine(format, arg);
        }
        public override void WriteLine(char[] buffer, int index, int count)
        {
            m_txtWritter.WriteLine(buffer, index, count);
        }
        public override void WriteLine(string format, object arg0, object arg1)
        {
            m_txtWritter.WriteLine(format, arg0, arg1);
        }
        public override void WriteLine(string format, object arg0, object arg1, object arg2)
        {
            m_txtWritter.WriteLine(format, arg0, arg1, arg2);
        }
        public override Task WriteLineAsync()
        {
            return m_txtWritter.WriteLineAsync();
        }
        public override Task WriteLineAsync(char value)
        {
            return m_txtWritter.WriteLineAsync(value);
        }
        public override Encoding Encoding
        {
            get 
            { 
                return m_txtWritter.Encoding; 
            }
        }
    }
}
