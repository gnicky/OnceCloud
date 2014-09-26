#pragma once

#include <string>
#include <vector>

using namespace std;

string & ReplaceString(string & source, const string & keyword, const string & replaceWith);
void SplitString(vector<string> & destination, const string & source, const string & delimiter);
