#pragma once

#include <string>
#include <vector>

void ReplaceString(std::string & source, const std::string & keyword, const std::string & replaceWith);
void SplitString(std::vector<std::string> & destination, const std::string & source, const std::string & delimiter);
